package com.tara.util.persistence.repo;

import com.tara.util.id.StringUID;
import com.tara.util.id.UID;
import com.tara.util.persistence.node.NodeType;
import com.tara.util.persistence.node.config.DBConfig;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.node.config.HTTPConfig;
import com.tara.util.persistence.node.config.NodeConfig;

import java.util.Map;

public interface Repository<VO> {
    RepositoryInfo getInfo();

    void globalDBConfig(DBConfig config);

    void globalHTTPConfig(HTTPConfig config);

    void globalFileConfig(FileConfig config);

    void defaultPriority(Priority priority);

    Priority defaultPriority();

    void update(Priority priority);

    void registerNode(UID id, NodeType type, NodeConfig config);

    void registerNode(UID id, NodeType type);

    void unregisterNode(UID id);

    void commit(UID id, VO value);

    VO checkout(UID id);

    void push(UID id);

    void fetch(UID id);

    void commitAll(Map<UID, VO> values);

    Map<UID, VO> checkoutAll();

    void pushAll();

    void fetchAll();

    default void update() {
        update(defaultPriority());
    }

    default void pushthrough(UID id, VO value) {
        commit(id, value);
        push(id);
    }

    default VO pull(UID id) {
        fetch(id);
        return checkout(id);
    }

    default void pushthroughAll(Map<UID, VO> values) {
        commitAll(values);
        pushAll();
    }

    default Map<UID, VO> pullAll() {
        fetchAll();
        return checkoutAll();
    }

    default UID registerNode(NodeType type, NodeConfig config) {
        UID id = new StringUID();
        registerNode(id, type, config);
        return id;
    }

    default UID registerNode(NodeType type) {
        UID id = new StringUID();
        registerNode(id, type);
        return id;
    }
}
