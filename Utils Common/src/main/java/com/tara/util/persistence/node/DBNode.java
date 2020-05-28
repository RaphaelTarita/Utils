package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.config.DBConfig;

public class DBNode<VO> extends AbstractNode<VO, DBConfig> {
    public DBNode(UID id, Class<VO> target, DBConfig config) {
        super(config, id, target);
    }

    public DBNode(Class<VO> target, DBConfig config) {
        super(config, target);
    }

    public DBNode(UID id, Class<VO> target) {
        super(DBConfig.defaultConf(), id, target);
    }

    public DBNode(Class<VO> target) {
        super(DBConfig.defaultConf(), target);
    }

    @Override
    public void push() {

    }

    @Override
    public void fetch() {

    }

    @Override
    protected void reloadConfig() {

    }
}