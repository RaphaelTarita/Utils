package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.config.NodeConfig;
import com.tara.util.persistence.node.state.NodeState;

public interface ResourceNode<VO> {
    default NodeType getType() {
        return NodeType.choose(this.getClass());
    }

    NodeState getState();

    UID id();

    void setConfig(NodeConfig config);

    void clear();

    void commit(VO vo);

    VO checkout();

    void push();

    void fetch();

    default void pushthrough(VO vo) {
        commit(vo);
        push();
    }

    default VO pull() {
        fetch();
        return checkout();
    }
}
