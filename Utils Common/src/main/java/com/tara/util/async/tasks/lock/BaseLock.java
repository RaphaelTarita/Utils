package com.tara.util.async.tasks.lock;

import com.tara.util.mirror.Mirrorable;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public abstract class BaseLock implements Mirrorable<BaseLock> {
    private boolean active;
    protected LocalDateTime since;

    public BaseLock() {
        active = false;
        since = LocalDateTime.now();
    }

    protected void activate() {
        active = true;
        since = LocalDateTime.now();
    }

    protected void renew() {
        if (active) {
            since = LocalDateTime.now();
        }
    }

    protected void activateOrRenew() {
        if (active) {
            renew();
        } else {
            activate();
        }
    }

    public void lift() {
        active = false;
    }

    public boolean locks() {
        return active;
    }

    @Override
    public abstract String toString();
}
