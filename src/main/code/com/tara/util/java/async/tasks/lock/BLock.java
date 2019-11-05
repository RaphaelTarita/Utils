package com.tara.util.java.async.tasks.lock;

public class BLock extends BaseLock {
    private Exception reason;

    public BLock() {
        super();
        reason = null;
    }

    public void activate(Exception reason) {
        activate();
        this.reason = reason;
    }

    public void renew(Exception reason) {
        renew();
        this.reason = reason;
    }

    public void activateOrRenew(Exception reason) {
        if (locks()) {
            renew(reason);
        } else {
            activate(reason);
        }
    }

    @Override
    public void lift() {
        super.lift();
        reason = null;
    }

    @Override
    public String toString() {
        return "BLock due to exception \'"
            + (reason != null
            ? reason.toString()
            : "<null>")
            + "\', since "
            + since.toString();
    }
}
