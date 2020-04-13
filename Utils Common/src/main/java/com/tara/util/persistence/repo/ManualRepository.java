package com.tara.util.persistence.repo;

import com.tara.util.persistence.node.config.DBConfig;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.node.config.HTTPConfig;

public class ManualRepository<VO> extends AbstractRepository<VO> {
    public ManualRepository(Class<VO> target, DBConfig dbConf, HTTPConfig httpConf, FileConfig fileConf) {
        super(target, dbConf, httpConf, fileConf);
    }

    public ManualRepository(Class<VO> target, DBConfig dbConf, HTTPConfig httpConf) {
        super(target, dbConf, httpConf);
    }

    public ManualRepository(Class<VO> target, DBConfig dbConf, FileConfig fileConf) {
        super(target, dbConf, fileConf);
    }

    public ManualRepository(Class<VO> target, HTTPConfig httpConf, FileConfig fileConf) {
        super(target, httpConf, fileConf);
    }

    public ManualRepository(Class<VO> target, DBConfig dbConf) {
        super(target, dbConf);
    }

    public ManualRepository(Class<VO> target, HTTPConfig httpConf) {
        super(target, httpConf);
    }

    public ManualRepository(Class<VO> target, FileConfig fileConf) {
        super(target, fileConf);
    }

    public ManualRepository(Class<VO> target) {
        super(target);
    }

    @Override
    public void update(Priority priority) {
        repo.forEach((id, node) -> updateSingle(node, priority));
    }
}
