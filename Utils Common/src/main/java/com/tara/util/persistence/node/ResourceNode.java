package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.Persistable;
import com.tara.util.persistence.node.config.NodeConfig;

public interface ResourceNode<VO extends Persistable> {
    enum Priority {
        REMOTE,
        LOCAL
    }

    default NodeType getType() {
        return NodeType.choose(this.getClass());
    }

    UID id();

    void setConfig(NodeConfig config);

    void setDefaultPriority(Priority priority);
    void deleteDefaultPriority();
    void resolve(Priority priority);
    boolean conflicted();

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
