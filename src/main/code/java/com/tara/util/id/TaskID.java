package com.tara.util.id;

import java.util.HashSet;
import java.util.Objects;

public class TaskID implements UID<String> {
    private static final long serialVersionUID = -71980682148021938L;

    private static HashSet<String> names = new HashSet<>();

    private String taskName;
    private HiLoIndex index;

    private void register(String name) {
        if (!taken(name)) {
            taskName = name;
            names.add(name);
        } else {
            throw new IllegalArgumentException("Name " + name + " or index " + index.toString() + " was already used");
        }
    }

    public TaskID(String name) {
        index = new HiLoIndex();
        register(name);
    }

    public String name() {
        return taskName;
    }

    public String index() {
        return index.toString();
    }

    @Override
    public boolean taken(String idGenerator) {
        return names.contains(idGenerator) && index.taken(index.toLong());
    }

    @Override
    public UID<String> newUnique(String generator) {
        return new TaskID(generator);
    }

    @Override
    public String getGenerator() {
        return taskName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof TaskID) {
            TaskID tidObj = (TaskID) obj;
            return tidObj.taskName.equals(taskName)
                    && tidObj.index.equals(index);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, index);
    }

    @Override
    public String toString() {
        return taskName + " (id: " + index + ')';
    }
}
