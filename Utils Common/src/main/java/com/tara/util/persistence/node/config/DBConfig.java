package com.tara.util.persistence.node.config;

public class DBConfig implements NodeConfig {
    public static DBConfig defaultConf() {
        return new DBConfig();
    }

    @Override
    public NodeConfig mirror() {
        return new DBConfig();
    }
}
