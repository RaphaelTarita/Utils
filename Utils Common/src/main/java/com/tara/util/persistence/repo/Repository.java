package com.tara.util.persistence.repo;

import com.tara.util.id.UID;
import com.tara.util.persistence.node.NodeType;
import com.tara.util.persistence.node.config.DBConfig;
import com.tara.util.persistence.node.config.HTTPConfig;
import com.tara.util.persistence.node.config.FileConfig;

public interface Repository<VO> {
    RepositoryInfo getInfo();

    void globalDBConfig(DBConfig config);
    void globalHTTPConfig(HTTPConfig config);
    void globalJSONConfig(FileConfig config);

    boolean registerNode(UID id, NodeType type);
}
