package com.tara.util.rest.node;

public abstract class RESTNode {
    private RESTNode parent;
    private String ext;

    public RESTNode(RESTNode parent, String ext) {
        this.parent = parent;
        this.ext = ext;
        if (parent instanceof DirectoryNode) {
            ((DirectoryNode) parent).registerChild(this);
        }
    }

    public RESTNode() {
        parent = this;
        ext = "";
    }

    public String ext() {
        return ext;
    }

    public RESTNode parent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == this;
    }
}
