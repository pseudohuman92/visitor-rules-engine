package com.visitor.game;

public final class ClockTestSupport {
    private ClockTestSupport() {}

    public static void expireOnNextTick(Clock clock) {
        clock.timeLeft = 1;
        clock.activate();
    }
}
