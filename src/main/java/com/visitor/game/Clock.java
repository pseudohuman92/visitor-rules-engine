package com.visitor.game;

import java.util.function.Consumer;

public class Clock extends Thread {

    enum ClockStatus {
        ACTIVE, PAUSED
    }


    volatile ClockStatus clockStatus;
    volatile int timeLeft;
    final Runnable callback;
    private final Consumer<Runnable> dispatch;


    public Clock(int setTimeSeconds, Runnable callback) {
        this(setTimeSeconds, callback, Runnable::run);
    }

    public Clock(int setTimeSeconds, Runnable callback, Consumer<Runnable> dispatch) {
        clockStatus = ClockStatus.PAUSED;
        timeLeft = setTimeSeconds * 1000;
        this.callback = callback;
        this.dispatch = dispatch;
    }

    @Override
    public void run() {
        while (timeLeft > 0) {
            try {
                Thread.sleep(Math.min(timeLeft, 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            dispatch.accept(this::tick);
        }
    }

    private void tick() {
        if (timeLeft <= 0) return;
        if (isActive()) timeLeft = Math.max(0, timeLeft - 1000);
        if (timeLeft == 0) {
            pause();
            callback.run();
        }
    }

    public void pause() {
        clockStatus = ClockStatus.PAUSED;
    }

    public void activate() {
        clockStatus = ClockStatus.ACTIVE;
    }

    public boolean isActive() {
        return clockStatus == ClockStatus.ACTIVE;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
