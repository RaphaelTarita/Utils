package com.tara.util.persistence.repo;

import com.tara.util.persistence.node.NodeType;
import com.tara.util.persistence.node.ResourceNode;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

public class RepositoryInfo {
    private final Set<NodeType> nodeTypes = EnumSet.noneOf(NodeType.class);

    public RepositoryInfo() {
    }

    public <VO> RepositoryInfo(Collection<ResourceNode<VO>> nodes) {
        for (ResourceNode<?> node : nodes) {
            nodeTypes.add(node.getType());
        }
    }

    public void addType(NodeType type) {
        nodeTypes.add(type);
    }

    public Set<NodeType> getNodeTypes() {
        return nodeTypes;
    }

    public RepositoryType overallType() {
        if (nodeTypes.size() == 1) {
            return RepositoryType.choose(nodeTypes.iterator().next());
        } else {
            return RepositoryType.MIXED;
        }
    }
}
