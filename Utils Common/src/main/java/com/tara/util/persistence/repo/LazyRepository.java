package com.tara.util.persistence.repo;

import com.tara.util.helper.date.DateHelper;
import com.tara.util.id.UID;
import com.tara.util.persistence.node.ResourceNode;
import com.tara.util.persistence.node.config.DBConfig;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.node.config.HTTPConfig;

import java.util.Map;

public class LazyRepository<VO> extends AbstractRepository<VO> {
    private final long expiryMillis;

    public LazyRepository(Class<VO> target, long expiryMillis, DBConfig dbConf, HTTPConfig httpConf, FileConfig fileConf) {
        super(target, dbConf, httpConf, fileConf);
        this.expiryMillis = expiryMillis;
    }

    public LazyRepository(Class<VO> target, long expiryMillis, DBConfig dbConf, HTTPConfig httpConf) {
        super(target, dbConf, httpConf);
        this.expiryMillis = expiryMillis;
    }

    public LazyRepository(Class<VO> target, long expiryMillis, DBConfig dbConf, FileConfig fileConf) {
        super(target, dbConf, fileConf);
        this.expiryMillis = expiryMillis;
    }

    public LazyRepository(Class<VO> target, long expiryMillis, HTTPConfig httpConf, FileConfig fileConf) {
        super(target, httpConf, fileConf);
        this.expiryMillis = expiryMillis;
    }

    public LazyRepository(Class<VO> target, long expiryMillis, DBConfig dbConf) {
        super(target, dbConf);
        this.expiryMillis = expiryMillis;
    }

    public LazyRepository(Class<VO> target, long expiryMillis, HTTPConfig httpConf) {
        super(target, httpConf);
        this.expiryMillis = expiryMillis;
    }

    public LazyRepository(Class<VO> target, long expiryMillis, FileConfig fileConf) {
        super(target, fileConf);
        this.expiryMillis = expiryMillis;
    }

    public LazyRepository(Class<VO> target, long expiryMillis) {
        super(target);
        this.expiryMillis = expiryMillis;
    }

    @Override
    public void update(Priority priority) {
        for (ResourceNode<VO> node : repo.values()) {
            if (System.currentTimeMillis() - DateHelper.getEpochMillis(node.getState().getTimestamp()) > expiryMillis) {
                updateSingle(node, priority);
            }
        }
    }

    @Override
    public void commit(UID id, VO value) {
        updateSingle(repo.get(id));
        super.commit(id, value);
    }

    @Override
    public VO checkout(UID id) {
        updateSingle(repo.get(id));
        return super.checkout(id);
    }

    @Override
    public void commitAll(Map<UID, VO> values) {
        update();
        super.commitAll(values);
    }

    @Override
    public Map<UID, VO> checkoutAll() {
        update();
        return super.checkoutAll();
    }
}
