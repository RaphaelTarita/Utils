package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.config.DBConfig;
import com.tara.util.persistence.node.config.NodeConfig;

public class DBNode<VO> extends AbstractNode<VO> {
    private DBConfig config;

    public DBNode(UID id, Class<VO> target, DBConfig config) {
        super(id, target);
        this.config = config;
    }

    public DBNode(Class<VO> target, DBConfig config) {
        super(target);
        this.config = config;
    }

    public DBNode(UID id, Class<VO> target) {
        super(id, target);
        this.config = DBConfig.defaultConf();
    }

    public DBNode(Class<VO> target) {
        super(target);
        this.config = DBConfig.defaultConf();
    }

    @Override
    public void setConfig(NodeConfig config) {
        if (config instanceof DBConfig) {
            this.config = (DBConfig) config;
        } else {
            throw new IllegalArgumentException(
                "Config class mismatch. Required: '"
                    + DBConfig.class.toString()
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
