package com.tara.util.persistence.node;

import com.tara.util.id.StringUID;
import com.tara.util.id.UID;
import com.tara.util.persistence.entity.JGPAEntity;
import com.tara.util.persistence.json.StringCodecRegistry;
import com.tara.util.persistence.node.state.NodeState;
import com.tara.util.persistence.node.state.NodeStateEnum;

public abstract class AbstractNode<VO> implements ResourceNode<VO> {
    protected final UID nodeID;
    protected final JGPAEntity<VO> gateway;
    protected final NodeState state;


    protected AbstractNode(UID id, Class<VO> target) {
        nodeID = id;
        gateway = new JGPAEntity<>(target);
        state = new NodeState(StringCodecRegistry.instance().getDateTimeFormat());
    }

    protected AbstractNode(Class<VO> target) {
        this(new StringUID(), target);
    }

    @Override
    public NodeState getState() {
        return state;
    }

    @Override
    public UID id() {
        return nodeID;
    }

    @Override
    public void clear() {
        gateway.detach();
        state.update(NodeStateEnum.EMPTY, "node content cleared");
    }

    @Override
    public void commit(VO vo) {
        gateway.bind(vo);
        state.update(NodeStateEnum.LOCAL, "committed local resource");
    }

    @Override
    public VO checkout() {
        if (gateway.detached()) {
            throw new IllegalStateException(
                "Cannot checkout empty node '"
                    + nodeID.mapUID()
                    + "'. Consider fetching before checking out"
            );
        } else {
            if (state.getState() == NodeStateEnum.REMOTE) {
                state.update(NodeStateEnum.SYNC, "checked out remote resource");
            }
            return gateway.getVO();
        }
    }
}
