package com.tara.util.async.tasks.lock;

import com.tara.util.mirror.Mirrors;

public class RLock extends BaseLock {
    private LockingAction action;

    public RLock() {
        super();
        action = null;
    }

    public void activate(LockingAction action) {
        activate();
        this.action = action;
    }

    public void renew(LockingAction action) {
        renew();
        this.action = action;
    }

    public void activateOrRenew(LockingAction action) {
        if (locks()) {
            renew(action);
        } else {
            activate(action);
        }
    }

    @Override
    public void lift() {
        super.lift();
        this.action = null;
    }

    @Override
    public String toString() {
        return "RLock due to action '"
                + (
                action != null
                        ? action.toString()
                        : "<null>"
        )
                + "', since "
                + since.toString();
    }

    @Override
    public RLock mirror() {
        RLock lock = new RLock();
        if (locks()) {
            lock.activate(action);
        }
        lock.since = Mirrors.mirror(since);
        return lock;
    }
}
