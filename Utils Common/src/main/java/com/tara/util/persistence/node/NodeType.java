package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.config.DBConfig;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.node.config.HTTPConfig;
import com.tara.util.persistence.node.config.NodeConfig;

public enum NodeType {
    DB(DBNode.class),
    HTTP(HTTPNode.class),
    FILE(FileNode.class),
    OTHER(ResourceNode.class);

    private final Class<?> clazz;

    NodeType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getTypeClass() {
        return clazz;
    }

    public static NodeType choose(Class<?> clazz) {
        if (DBNode.class.equals(clazz)) {
            return DB;
        } else if (HTTPNode.class.equals(clazz)) {
            return HTTP;
        } else if (FileNode.class.equals(clazz)) {
            return FILE;
        }
        return OTHER;
    }

    public NodeConfig getConfig() {
        switch (this) {
            case DB:
                return DBConfig.defaultConf();
            case HTTP:
                return HTTPConfig.defaultConf();
            case FILE:
                return FileConfig.defaultConf();
            case OTHER:
            default:
                throw new IllegalArgumentException("Could not find default config for NodeType '" + toString() + '\'');
        }
    }

    public <VO> ResourceNode<VO> getNode(UID id, Class<VO> target, NodeConfig config) {
        ResourceNode<VO> res;
        switch (this) {
            case DB:
                res = new DBNode<>(id, target);
                break;
            case HTTP:
                res = new HTTPNode<>(id, target);
                break;
            case FILE:
                res = new FileNode<>(id, target);
                break;
            case OTHER:
            default:
                throw new IllegalArgumentException("Could not find constructor for NodeType '" + toString() + '\'');
        }
        res.setConfig(config);
        return res;
    }

    public <VO> ResourceNode<VO> getNode(UID id, Class<VO> target) {
        return getNode(id, target, getConfig());
    }
}
