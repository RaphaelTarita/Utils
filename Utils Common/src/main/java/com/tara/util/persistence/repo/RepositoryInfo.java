package com.tara.util.persistence.repo;

import com.tara.util.persistence.node.NodeType;

import java.util.EnumSet;
import java.util.Set;

public class RepositoryInfo {
    private Set<NodeType> nodeTypes = EnumSet.noneOf(NodeType.class);

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
