package com.tara.util.persistence.node.config;

public class HTTPConfig implements NodeConfig {
    public static HTTPConfig defaultConf() {
        return new HTTPConfig();
    }

    @Override
    public NodeConfig mirror() {
        return new HTTPConfig();
    }
}
