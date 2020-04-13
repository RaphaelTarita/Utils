package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.json.FormatException;
import com.tara.util.persistence.json.JSONPort;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.node.config.NodeConfig;
import com.tara.util.persistence.node.state.NodeStateEnum;

import java.io.*;

public class FileNode<VO> extends AbstractNode<VO> {
    private FileConfig config;
    private File file;

    public FileNode(UID id, Class<VO> target, FileConfig config) {
        super(id, target);
        this.config = config;
        resolveFile();
    }

    public FileNode(Class<VO> target, FileConfig config) {
        super(target);
        this.config = config;
        resolveFile();
    }

    public FileNode(UID id, Class<VO> target) {
        super(id, target);
        this.config = FileConfig.defaultConf();
        resolveFile();
    }

    public FileNode(Class<VO> target) {
        super(target);
        this.config = FileConfig.defaultConf();
        resolveFile();
    }

    @Override
    public void setConfig(NodeConfig config) {
        if (config instanceof FileConfig) {
            this.config = (FileConfig) config;
            resolveFile();
        } else {
            throw new IllegalArgumentException(
                "Config class mismatch. Required: '"
                    + FileConfig.class.toString()
                    + "', Given: '" + config.getClass().toString()
                    + '\''
            );
        }
    }

    @Override
    public void push() {
        try {
            if (file.createNewFile()) {
                state.update("created new file: " + file.getAbsolutePath());
            }
        } catch (IOException ex) {
            exc(ex, "push");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (gateway.detached()) {
                if (!file.delete()) {
                    throw new IOException("could not delete file");
                }
            }

            String json = JSONPort.toJSON(gateway);
            writer.write(json);
            if (state.getState() == NodeStateEnum.LOCAL) {
                state.update(NodeStateEnum.SYNC, "push successful");
            }
        } catch (FileNotFoundException ex) {
            fileNotFound(ex);
        } catch (IOException ex) {
            exc(ex, "push");
        }
    }

    @Override
    public void fetch() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            gateway.bindEmpty();
            String json = readAll(reader);
            JSONPort.fromJSON(gateway, json);
            state.update(NodeStateEnum.REMOTE, "fetch successful");
        } catch (FileNotFoundException ex) {
            fileNotFound(ex);
        } catch (IOException | FormatException ex) {
            exc(ex, "fetch");
        }
    }

    private void resolveFile() {
        file = config.getFile(nodeID.mapUID());
    }

    private void fileNotFound(FileNotFoundException ex) {
        gateway.detach();
        state.update(NodeStateEnum.EMPTY, "file not found, cleared node: " + ex.toString());
    }

    private void exc(Exception ex, String action) {
        state.update(NodeStateEnum.ERROR, "error occurred during" + action + ": " + ex.toString());
    }

    private String readAll(BufferedReader reader) throws IOException {
        StringBuilder res = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            res.append(line);
        }
        return res.toString();
    }
}
