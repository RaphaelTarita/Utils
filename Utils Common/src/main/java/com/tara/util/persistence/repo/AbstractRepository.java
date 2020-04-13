package com.tara.util.persistence.repo;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.NodeType;
import com.tara.util.persistence.node.ResourceNode;
import com.tara.util.persistence.node.config.DBConfig;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.node.config.HTTPConfig;
import com.tara.util.persistence.node.config.NodeConfig;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRepository<VO> implements Repository<VO> {
    protected final Map<UID, ResourceNode<VO>> repo = new HashMap<>();
    protected final Class<VO> target;

    private DBConfig globalDBConfig;
    private HTTPConfig globalHTTPConfig;
    private FileConfig globalFileConfig;

    private Priority defaultPriority;

    protected static <VO> void updateSingle(ResourceNode<VO> node, Priority priority) {
        switch (node.getState().getState()) {
            case LOCAL:
                node.push();
                break;
            case REMOTE:
                node.fetch();
                break;
            case SYNC:
            case EMPTY:
            case ERROR:
            default:
                priority.executeOn(node);
        }
    }

    protected void updateSingle(ResourceNode<VO> node) {
        if (node != null) {
            updateSingle(node, defaultPriority);
        }
    }

    protected AbstractRepository(Class<VO> target, DBConfig dbConf, HTTPConfig httpConf, FileConfig fileConf) {
        this.target = target;

        globalDBConfig = dbConf;
        globalHTTPConfig = httpConf;
        globalFileConfig = fileConf;

        defaultPriority = Priority.REMOTE;
    }

    protected AbstractRepository(Class<VO> target, DBConfig dbConf, HTTPConfig httpConf) {
        this(target, dbConf, httpConf, null);
    }

    protected AbstractRepository(Class<VO> target, DBConfig dbConf, FileConfig fileConf) {
        this(target, dbConf, null, fileConf);
    }

    protected AbstractRepository(Class<VO> target, HTTPConfig httpConf, FileConfig fileConf) {
        this(target, null, httpConf, fileConf);
    }

    protected AbstractRepository(Class<VO> target, DBConfig dbConf) {
        this(target, dbConf, null, null);
    }

    protected AbstractRepository(Class<VO> target, HTTPConfig httpConf) {
        this(target, null, httpConf, null);
    }

    protected AbstractRepository(Class<VO> target, FileConfig fileConf) {
        this(target, null, null, fileConf);
    }

    protected AbstractRepository(Class<VO> target) {
        this(target, null, null, null);
    }

    @Override
    public void globalDBConfig(DBConfig config) {
        globalDBConfig = config;
    }

    @Override
    public void globalHTTPConfig(HTTPConfig config) {
        globalHTTPConfig = config;
    }

    @Override
    public void globalFileConfig(FileConfig config) {
        globalFileConfig = config;
    }

    @Override
    public void defaultPriority(Priority priority) {
        defaultPriority = priority;
    }

    @Override
    public Priority defaultPriority() {
        return defaultPriority;
    }

    protected NodeConfig resolveDefaultConfig(NodeType type) {
        NodeConfig config = type.getConfig();
        if (type == NodeType.DB && globalDBConfig != null) {
            config = globalDBConfig;
        } else if (type == NodeType.HTTP && globalHTTPConfig != null) {
            config = globalHTTPConfig;
        } else if (type == NodeType.FILE && globalFileConfig != null) {
            config = globalFileConfig;
        }
        return config;
    }

    @Override
    public void registerNode(UID id, NodeType type, NodeConfig config) {
        repo.put(id, type.getNode(id, target, config));
    }

    @Override
    public void registerNode(UID id, NodeType type) {
        repo.put(id, type.getNode(id, target, resolveDefaultConfig(type)));
    }

    @Override
    public void unregisterNode(UID id) {
        repo.remove(id);
    }

    @Override
    public void commit(UID id, VO value) {
        ResourceNode<VO> node = repo.get(id);
        if (node != null) {
            node.commit(value);
        }
    }

    @Override
    public VO checkout(UID id) {
        ResourceNode<VO> node = repo.get(id);
        if (node != null) {
            return node.checkout();
        } else {
            return null;
        }
    }

    @Override
    public void push(UID id) {
        ResourceNode<VO> node = repo.get(id);
        if (node != null) {
            node.push();
        }
    }

    @Override
    public void fetch(UID id) {
        ResourceNode<VO> node = repo.get(id);
        if (node != null) {
            node.fetch();
        }
    }

    @Override
    public void commitAll(Map<UID, VO> values) {
        for (Map.Entry<UID, VO> entry : values.entrySet()) {
            commit(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Map<UID, VO> checkoutAll() {
        Map<UID, VO> res = new HashMap<>(repo.size());
        for (Map.Entry<UID, ResourceNode<VO>> entry : repo.entrySet()) {
            res.put(entry.getKey(), checkout(entry.getKey()));
        }
        return res;
    }

    @Override
    public void pushAll() {
        repo.forEach((id, node) -> node.push());
    }

    @Override
    public void fetchAll() {
        repo.forEach((id, node) -> node.fetch());
    }

    public RepositoryInfo getInfo() {
        return new RepositoryInfo(repo.values());
    }
}
