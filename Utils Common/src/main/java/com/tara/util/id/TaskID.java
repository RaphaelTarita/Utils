package com.tara.util.id;

import java.util.Objects;
import java.util.function.Function;

public class TaskID implements UID {
    private static final long serialVersionUID = -71980682148021938L;


    private String taskName;
    private HiLoIndex index;

    private TaskID(String name, HiLoIndex index) {
        this.index = index;
        this.taskName = name;
        register();
    }

    public TaskID(String name) {
        index = new HiLoIndex();
        taskName = name;
        register();
    }

    @Override
    public Function<String, UID> stringConverter() {
        return s -> {
            String[] args = s.split("\\s");
            String index = args[1].replace(" (id:", "")
                .replace(")", "");
            return new TaskID(args[0], new HiLoIndex(index));
        };
    }

    public String name() {
        return taskName;
    }

    public String index() {
        return index.toString();
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
