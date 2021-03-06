package com.tara.util.id;

import com.tara.util.mirror.Mirrorable;

import java.util.Objects;

public class TaskID implements UID, Mirrorable<TaskID> {
    private static final long serialVersionUID = -71980682148021938L;


    private String taskName;
    private HiLoIndex index;

    static {
        UID.registerBuilder(TaskID.class, TaskID::mapString);
    }

    public static TaskID mapString(String str) {
        String[] args = str.split("\\s");
        String name = args[0].replace("_", " ");
        return new TaskID(name, HiLoIndex.mapString(args[1]));
    }

    private TaskID(String name, HiLoIndex index) {
        this.index = index;
        this.taskName = name;
        registerUID();
    }

    public TaskID(String name) {
        index = new HiLoIndex();
        taskName = name;
        registerUID();
    }

    @Override
    public String mapUID() {
        return taskName.replace(" ", "_") + " " + index.mapUID();
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

    @Override
    public TaskID mirror() {
        return new TaskID(taskName, index.mirror());
    }
}
