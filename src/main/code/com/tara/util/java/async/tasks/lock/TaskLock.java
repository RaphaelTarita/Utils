package com.tara.util.java.async.tasks.lock;

import com.tara.util.java.async.tasks.TaskID;

public class TaskLock {
    private final TaskID on;
    private RLock rlock;
    private BLock block;

    public TaskLock(TaskID on) {
        this.on = on;
        rlock = new RLock();
        block = new BLock();
    }

    public void lockOnAction(LockingAction action) {
        rlock.activate(action);
    }

    public void renewOnAction(LockingAction action) {
        rlock.renew(action);
    }

    public void lockOrRenewOnAction(LockingAction action) {
        rlock.activateOrRenew(action);
    }

    public void lockOnException(Exception cause) {
        block.activate(cause);
    }

    public void renewOnException(Exception cause) {
        block.renew(cause);
    }

    public void lockOrRenewOnException(Exception cause) {
        block.activateOrRenew(cause);
    }

    public void liftRLock() {
        rlock.lift();
    }

    public void liftBLock() {
        block.lift();
    }

    public void lift() {
        liftRLock();
        liftBLock();
    }

    public boolean rlocks() {
        return rlock.locks();
    }

    public boolean blocks() {
        return block.locks();
    }

    public boolean locks() {
        return rlocks() || blocks();
    }

    @Override
    public String toString() {
        return "TaskLock on "
                + on.toString()
                + ": "
                + (
                rlocks()
                        ? rlock.toString()
                        : "<RLock inactive>"
        )
                + ", "
                + (
                blocks()
                        ? block.toString()
                        : "<BLock inactive>"
        );
    }
}
