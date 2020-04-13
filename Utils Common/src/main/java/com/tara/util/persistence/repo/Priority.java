package com.tara.util.persistence.repo;

import com.tara.util.persistence.node.ResourceNode;

import java.util.function.Consumer;

public enum Priority {
    LOCAL(ResourceNode::push),
    REMOTE(ResourceNode::fetch);

    private final Consumer<ResourceNode<?>> operation;

    Priority(Consumer<ResourceNode<?>> operation) {
        this.operation = operation;
    }

    public void executeOn(ResourceNode<?> node) {
        operation.accept(node);
    }
}
