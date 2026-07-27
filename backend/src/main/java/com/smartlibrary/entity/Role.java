package com.smartlibrary.entity;

/**
 * Application roles used for role-based authorization.
 * Stored as a string column on the {@code users} table.
 */
public enum Role {
    ADMIN,
    LIBRARIAN,
    USER
}
