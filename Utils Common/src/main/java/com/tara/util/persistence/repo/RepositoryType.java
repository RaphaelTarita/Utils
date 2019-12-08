package com.tara.util.persistence.repo;

import com.tara.util.persistence.node.NodeType;

public enum RepositoryType {
    DB(NodeType.DB),
    HTTP(NodeType.HTTP),
    JSON(NodeType.JSON),
    OTHER(NodeType.OTHER),
    MIXED(NodeType.OTHER);

    private final NodeType type;

    RepositoryType(NodeType type) {
        this.type = type;
    }

    public NodeType getType() {
        return type;
    }

    public Class<?> getTypeClass() {
        return type.getTypeClass();
    }

    public static RepositoryType choose(NodeType type) {
        switch (type) {
            case DB:
                return DB;
            case HTTP:
                return HTTP;
            case JSON:
                return JSON;
            case OTHER:
                return OTHER;
            default:
                return MIXED;
        }
    }
}
