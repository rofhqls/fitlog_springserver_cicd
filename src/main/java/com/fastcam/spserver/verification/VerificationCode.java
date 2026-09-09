package com.fastcam.spserver.verification;

import lombok.Getter;

public class VerificationCode {
    @Getter
    private final int code;
    private final long expireTime;

    public VerificationCode(int code) {
        this.code = code;
        this.expireTime = System.currentTimeMillis() + 5 * 60 * 1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }
}
