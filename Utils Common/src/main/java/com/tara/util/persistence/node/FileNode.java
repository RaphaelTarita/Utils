package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.json.FormatException;
import com.tara.util.persistence.json.JSONConvert;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.node.state.NodeStateEnum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileNode<VO> extends AbstractNode<VO, FileConfig> {
    private File file;

    public FileNode(UID id, Class<VO> target, FileConfig config) {
        super(config, id, target);
    }

    public FileNode(Class<VO> target, FileConfig config) {
        super(config, target);
    }

    public FileNode(UID id, Class<VO> target) {
        super(FileConfig.defaultConf(), id, target);
    }

    public FileNode(Class<VO> target) {
        super(FileConfig.defaultConf(), target);
    }

    @Override
    public void push() {
        try {
            if (file.createNewFile()) {
                state.update(NodeAction.PUSH, "created new file: " + file.getAbsolutePath());
            }
        } catch (IOException ex) {
            exc(ex, NodeAction.PUSH);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (gateway.detached()) {
                if (!file.delete()) {
                    throw new IOException("could not delete file");
                }
            }

            String json = JSONConvert.toJSON(gateway);
            writer.write(json);
            if (state.getState() == NodeStateEnum.LOCAL) {
                state.update(NodeAction.PUSH, NodeStateEnum.SYNC, "push successful");
            }
        } catch (FileNotFoundException ex) {
            fileNotFound(ex);
        } catch (IOException ex) {
            exc(ex, NodeAction.PUSH);
        }
    }

    @Override
    public void fetch() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            gateway.bindEmpty();
            String json = readAll(reader);
            JSONConvert.fromJSON(gateway, json);
            state.update(NodeAction.FETCH, NodeStateEnum.REMOTE, "fetch successful");
        } catch (FileNotFoundException ex) {
            fileNotFound(ex);
        } catch (IOException | FormatException ex) {
            exc(ex, NodeAction.FETCH);
        }
    }

    @Override
    protected void reloadConfig() {
        file = config.getFile(nodeID.mapUID());
    }

    private void fileNotFound(FileNotFoundException ex) {
        gateway.detach();
        state.update(NodeAction.OTHER, NodeStateEnum.EMPTY, "file not found, cleared node: " + ex.toString());
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