package com.tara.util.persistence.node;

public enum NodeType {
    DB(DBNode.class),
    HTTP(HTTPNode.class),
    JSON(JSONNode.class),
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
        } else if (JSONNode.class.equals(clazz)) {
            return JSON;
        }
        return OTHER;
    }
}
