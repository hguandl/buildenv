package com.hguandl.buildenv.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SandboxConfig {
    @Value("${buildenv.util.sandbox.life}")
    private long life;

    @Value("${buildenv.util.sandbox.cnt}")
    private int cnt;

    public long getLife() {
        return life;
    }

    public int getCnt() {
        return cnt;
    }
}
