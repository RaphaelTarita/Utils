package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.config.HTTPConfig;
import com.tara.util.persistence.node.config.NodeConfig;

public class HTTPNode<VO> extends AbstractNode<VO> {
    private HTTPConfig config;

    public HTTPNode(UID id, Class<VO> target, HTTPConfig config) {
        super(id, target);
        this.config = config;
    }

    public HTTPNode(Class<VO> target, HTTPConfig config) {
        super(target);
        this.config = config;
    }

    public HTTPNode(UID id, Class<VO> target) {
        super(id, target);
        this.config = HTTPConfig.defaultConf();
    }

    public HTTPNode(Class<VO> target) {
        super(target);
        this.config = HTTPConfig.defaultConf();
    }

    @Override
    public void setConfig(NodeConfig config) {
        if (config instanceof HTTPConfig) {
            this.config = (HTTPConfig) config;
        } else {
            throw new IllegalArgumentException(
                "Config class mismatch. Required: '"
                    + HTTPConfig.class.toString()
                    + "', Given: '" + config.getClass().toString()
                    + '\''
            );
        }
    }

    @Override
    public void push() {

    }

    @Override
    public void fetch() {

    }
}
