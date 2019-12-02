package com.tara.util.async.tasks.lock;

import com.tara.util.mirror.Mirrors;

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
        return "BLock due to exception '"
                + (
                reason != null
                        ? reason.toString()
                        : "<null>"
        )
                + "', since "
                + since.toString();
    }

    @Override
    public BLock mirror() {
        BLock lock = new BLock();
        if (locks()) {
            lock.activate(Mirrors.mirror(reason));
        }
        lock.since = Mirrors.mirror(since);
        return lock;
    }
}
