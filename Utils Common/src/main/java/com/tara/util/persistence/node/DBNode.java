package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.config.NodeConfig;

public class DBNode<VO> extends AbstractNode<VO> {
    protected DBNode(UID nodeID, Class<? extends VO> voClass, Priority defaultP) {
        super(nodeID, voClass, defaultP);
    }

    protected DBNode(UID nodeID, Class<? extends VO> voClass) {
        super(nodeID, voClass);
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
