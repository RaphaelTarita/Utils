package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.json.FormatException;
import com.tara.util.persistence.json.JSONPort;
import com.tara.util.persistence.node.config.JSONConfig;
import com.tara.util.persistence.node.config.NodeConfig;
import com.tara.util.persistence.node.state.NodeStateEnum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class JSONNode<VO> extends AbstractNode<VO> {
    private JSONConfig config;
    private File target;

    public JSONNode(UID nodeID, Class<? extends VO> voClass, Priority defaultP) {
        super(nodeID, voClass, defaultP);
    }

    public JSONNode(UID nodeID, Class<? extends VO> voClass, Priority defaultP, JSONConfig config) {
        this(nodeID, voClass, defaultP);
        this.config = config;
        target = null;
    }

    public JSONNode(UID nodeID, Class<? extends VO> voClass) {
        super(nodeID, voClass);
    }

    public JSONNode(UID nodeID, Class<? extends VO> voClass, JSONConfig config) {
        this(nodeID, voClass);
        this.config = config;
        target = null;
    }

    @Override
    public void setConfig(NodeConfig config) {
        if (config instanceof JSONConfig) {
            this.config = (JSONConfig) config;
        } else {
            throw new IllegalArgumentException("Wrong config type for JSONNode: " + config.getClass());
        }
    }

    @Override
    public void push() {
        requireSync();
        resolveTarget();
        gateway.bind(liveVO);
        String json = JSONPort.toJSON(gateway);
        try {
            Files.deleteIfExists(target.toPath());
            if (target.getParentFile() != null) {
                target.getParentFile().mkdirs();
            }
            target.createNewFile();
        } catch (IOException ex) {
            state.update(NodeStateEnum.PROBLEM, "IOException encountered while PUSH preprocessing, check config. (Exception: " + ex.toString() + ")");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(target))) {
            writer.write(json);
        } catch (IOException ex) {
            state.update(NodeStateEnum.PROBLEM, "IOException encountered while PUSH, check config. (Exception: " + ex.toString() + ")");
        }
    }

    @Override
    public void fetch() {
        requireNonConflicted();
        resolveTarget();
        unresolvedRemote = gateway.getVO();
        gateway.bind(unresolvedRemote);
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(target))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (FileNotFoundException ex) {
            state.update(NodeStateEnum.DETACHED, "File not found");
        } catch (IOException ex) {
            state.update(NodeStateEnum.PROBLEM, "IOException encountered while FETCH, check config. (Exception: " + ex.toString() + ")");
        }
        try {
            JSONPort.fromJSON(gateway, json.toString());
        } catch (FormatException ex) {
            state.update(NodeStateEnum.PROBLEM, "JSON has invalid format: " + ex.toString());
        }
        state.update(NodeStateEnum.REMOTE, "Fetched from remote");
        sync();
    }

    private void requireConfig() {
        if (config == null) {
            throw new IllegalArgumentException("Resource Node is not configured");
        }
    }

    private void resolveTarget() {
        if (target == null) {
            requireConfig();
            target = config.getFile(nodeID.mapUID());
        }
    }
}
