package com.smartlibrary.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory store of invalidated JWTs (logout). In production this would be
 * backed by Redis with TTL equal to the token's remaining lifetime.
 */
@Component
public class TokenBlacklist {

    private final Map<String, Long> blacklisted = new ConcurrentHashMap<>();

    public void blacklist(String token, long expiryEpochMs) {
        blacklisted.put(token, expiryEpochMs);
    }

    public boolean isBlacklisted(String token) {
        Long expiry = blacklisted.get(token);
        if (expiry == null) {
            return false;
        }
        if (expiry < System.currentTimeMillis()) {
            blacklisted.remove(token);
            return false;
        }
        return true;
    }
}
