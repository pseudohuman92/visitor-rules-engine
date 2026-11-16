package com.visitor.game;

import java.util.UUID;

/** A rejected client command, detected before applying its game-state changes. */
public class IllegalGameActionException extends IllegalArgumentException {
    public IllegalGameActionException(String message) {
        super(message);
    }

    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalGameActionException(message);
        }
    }

    public static UUID parseId(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalGameActionException("Invalid object ID");
        }
    }
}
