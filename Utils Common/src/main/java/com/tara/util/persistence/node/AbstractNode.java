package com.tara.util.persistence.node;

import com.tara.util.id.StringUID;
import com.tara.util.id.UID;
import com.tara.util.persistence.entity.JGPAEntity;
import com.tara.util.persistence.json.StringCodecRegistry;
import com.tara.util.persistence.node.config.NodeConfig;
import com.tara.util.persistence.node.state.NodeState;
import com.tara.util.persistence.node.state.NodeStateEnum;

public abstract class AbstractNode<VO, C extends NodeConfig> implements ResourceNode<VO> {
    protected final UID nodeID;
    protected final JGPAEntity<VO> gateway;
    protected final NodeState state;

    protected C config;

    private final Class<?> configType;


    protected AbstractNode(C config, UID id, Class<VO> target) {
        this.configType = config.getClass();
        this.config = config;
        nodeID = id;
        gateway = new JGPAEntity<>(target);
        state = new NodeState(StringCodecRegistry.instance().getDateTimeFormat());
        reloadConfig();
    }

    protected AbstractNode(C config, Class<VO> target) {
        this(config, new StringUID(), target);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setConfig(NodeConfig config) {
        if (configType.isInstance(config)) {
            this.config = (C) config;
            reloadConfig();
        } else {
            throw new IllegalArgumentException(
                "Config class mismatch. Required: '"
                    + configType.toString()
                    + "', Given: '"
                    + config.getClass().toString()
                    + '\''
            );
        }
    }

    protected abstract void reloadConfig();

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
        state.update(NodeAction.OTHER.setAlt("delete"), NodeStateEnum.EMPTY, "node content cleared");
    }

    @Override
    public void commit(VO vo) {
        gateway.bind(vo);
        state.update(NodeAction.COMMIT, NodeStateEnum.LOCAL, "committed local resource");
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
                state.update(NodeAction.CHECKOUT, NodeStateEnum.SYNC, "checked out remote resource");
            }
            return gateway.getVO();
        }
    }

    protected void exc(Exception ex, NodeAction action) {
        state.update(action, NodeStateEnum.ERROR, "error occurred: " + ex.toString());
    }
}