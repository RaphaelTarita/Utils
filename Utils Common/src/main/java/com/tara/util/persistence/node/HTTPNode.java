package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.config.NodeConfig;

public class HTTPNode<VO> extends AbstractNode<VO> {
    protected HTTPNode(UID nodeID, Priority defaultP) {
        super(nodeID, defaultP);
    }

    protected HTTPNode(UID nodeID) {
        super(nodeID);
    }

    @Override
    public UID id() {
        return null;
    }

    @Override
    public void setConfig(NodeConfig config) {

    }

    @Override
    public void commit(VO vo) {

    }

    @Override
    public VO checkout() {
        return null;
    }

    @Override
    public void push() {

    }

    @Override
    public void fetch() {

    }
}
