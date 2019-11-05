package com.tara.util.java.async.tasks.lock;

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
        return "RLock due to action \'"
            + (action != null
            ? action.toString()
            : "<null>")
            + "\', since "
            + since.toString();
    }
}
