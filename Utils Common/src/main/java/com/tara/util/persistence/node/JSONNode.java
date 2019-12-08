package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.config.JSONConfig;
import com.tara.util.persistence.node.config.NodeConfig;

public class JSONNode<VO> extends AbstractNode<VO> {
    private JSONConfig config;

    public JSONNode(UID nodeID, Priority defaultP) {
        super(nodeID, defaultP);
    }

    public JSONNode(UID nodeID, Priority defaultP, JSONConfig config) {
        this(nodeID, defaultP);
        this.config = config;
    }

    public JSONNode(UID nodeID) {
        super(nodeID);
    }

    public JSONNode(UID nodeID, JSONConfig config) {
        this(nodeID);
        this.config = config;
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

    }

    @Override
    public void fetch() {

    }
}
