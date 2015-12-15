package com.github.drxaos.robo3d.graphics;

public abstract class FutureCallback {

    private float mTimeRemaining;

    public FutureCallback(float timeSec) {
        mTimeRemaining = timeSec;
    }

    boolean decreaseTime(float timeSinceLastFrame) {
        mTimeRemaining -= timeSinceLastFrame;

        if (mTimeRemaining <= 0.0f) {
            onFutureHappened();
            return true;
        } else {
            return false;
        }
    }

    public abstract void onFutureHappened();
}
