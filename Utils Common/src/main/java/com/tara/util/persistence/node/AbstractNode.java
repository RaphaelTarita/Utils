package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.field.JGPAEntity;
import com.tara.util.persistence.node.state.NodeState;
import com.tara.util.persistence.node.state.NodeStateEnum;

public abstract class AbstractNode<VO> implements ResourceNode<VO> {
    protected final UID nodeID;
    private Priority defaultP;
    protected VO liveVO;
    protected VO unresolvedRemote;
    protected VO unresolvedLocal;
    protected JGPAEntity<VO> gateway;
    protected NodeState state;

    protected AbstractNode(UID nodeID, Class<? extends VO> voClass, Priority defaultP) {
        this.nodeID = nodeID;
        this.defaultP = defaultP;
        liveVO = null;
        unresolvedRemote = null;
        unresolvedLocal = null;
        gateway = new JGPAEntity<>(voClass);
        state = new NodeState(nodeID);
    }

    protected AbstractNode(UID nodeID, Class<? extends VO> voClass) {
        this(nodeID, voClass, null);
    }

    protected void sync() {
        switch (state.getEnumeratedState()) {
            case REMOTE:
                unresolvedLocal = null;
                liveVO = unresolvedRemote;
                break;
            case LOCAL:
                unresolvedRemote = null;
                liveVO = unresolvedLocal;
                break;
            case SYNC:
                return;
            case DETACHED:
            case UNKNOWN:
            case CONFLICT:
            default:
                throw new IllegalStateException("Illegal state for sync(): " + state.getEnumeratedState().str());
        }
        state.update(NodeStateEnum.SYNC, "synced from previous live channel");
    }

    @Override
    public void setDefaultPriority(Priority priority) {
        defaultP = priority;
    }

    @Override
    public void deleteDefaultPriority() {
        defaultP = null;
    }

    @Override
    public void resolve(Priority priority) {
        switch (priority) {
            case REMOTE:
                liveVO = unresolvedRemote;
                state.update(NodeStateEnum.REMOTE, "resolved with priority REMOTE");
                break;
            case LOCAL:
                liveVO = unresolvedLocal;
                state.update(NodeStateEnum.LOCAL, "resolved with priority LOCAL");
                break;
            default:
                throw new IllegalArgumentException("priority " + priority.name() + " not known.");
        }
        sync();
    }

    protected void requireNonConflicted() {
        if (conflicted()) {
            throw new IllegalStateException("Resource Node is conflicted, history:\n" + state.getHistory());
        }
    }

    protected void requireSync() {
        if (state.getEnumeratedState() != NodeStateEnum.SYNC) {
            throw new IllegalStateException("Resource Node is not in sync, history:\n" + state.getHistory());
        }
    }

    @Override
    public boolean conflicted() {
        return state.getEnumeratedState() == NodeStateEnum.CONFLICT;
    }

    @Override
    public UID id() {
        return nodeID;
    }

    @Override
    public void commit(VO vo) {
        unresolvedLocal = vo;
        if (state.getEnumeratedState() == NodeStateEnum.REMOTE) {
            state.update(NodeStateEnum.CONFLICT, "commit failed, REMOTE is live. Will attempt to resolve with default priority if present");
            if (defaultP != null) {
                resolve(defaultP);
            }
        } else {
            state.update(NodeStateEnum.LOCAL, "commit successful");
            sync();
        }
    }

    @Override
    public VO checkout() {
        requireSync();
        return liveVO;
    }
}
