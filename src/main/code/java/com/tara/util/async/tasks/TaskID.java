package com.tara.util.async.tasks;

import com.tara.util.id.HiLoIndex;

import java.io.Serializable;
import java.util.Objects;

public class TaskID implements Serializable {
    private static final long serialVersionUID = -71980682148021938L;

    private String taskName;
    private HiLoIndex index;

    public TaskID(String name) {
        taskName = name;
        index = new HiLoIndex();
    }

    String name() {
        return taskName;
    }

    String index() {
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
