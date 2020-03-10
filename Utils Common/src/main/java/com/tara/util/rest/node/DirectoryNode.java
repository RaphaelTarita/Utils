package com.tara.util.rest.node;

import java.util.HashMap;
import java.util.Map;

public class DirectoryNode extends RESTNode {
    private Map<String, RESTNode> children;

    public DirectoryNode(RESTNode parent, String ext) {
        super(parent, ext);
        children = new HashMap<>();
    }

    public void registerChild(RESTNode child) {
        children.put(child.ext(), child);
    }
}
