package com.github.drxaos.robo3d.scripts;

public class Data {

    public float x;
    public float y;
    public float angle;
    public float turret;
    public float distance;
    public float type;

    public float chassisLeft;
    public float chassisRight;
    public boolean fire;
    public float scanAngle;

    public float state;

    // IN
    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float angle() {
        return angle;
    }

    public float turret() {
        return turret;
    }

    public float distance() {
        return distance;
    }

    public float type() {
        return type;
    }

    public float state() {
        return state;
    }

    // OUT
    public void chassis_left(float max100) {
        chassisLeft = Math.max(100, Math.min(-100, max100));
    }

    public void chassis_right(float max100) {
        chassisRight = Math.max(100, Math.min(-100, max100));
    }

    public void fire() {
        fire = true;
    }

    public void scan(float angle) {
        scanAngle = angle;
    }

    public void state(float memorize) {
        state = memorize;
    }

}
