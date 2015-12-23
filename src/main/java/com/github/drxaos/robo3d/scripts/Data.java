package com.github.drxaos.robo3d.scripts;

public class Data {

    public float x;
    public float y;
    public float angle;
    public float turret;
    public float armor;
    public float distance;
    public int type;

    public float chassisLeft;
    public float chassisRight;
    public boolean fire;
    public float scanAngle;

    public float rx0, rx1;
    public int dx0, dx1;

    public int channel, send, recv;

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

    public float armor() {
        return armor;
    }

    public float distance() {
        return distance;
    }

    public int type() {
        return type;
    }

    public float rx0() {
        return rx0;
    }

    public float rx1() {
        return rx1;
    }

    public int dx0() {
        return dx0;
    }

    public int dx1() {
        return dx1;
    }

    public int recv() {
        return recv;
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

    public void rx0(float memorize) {
        rx0 = memorize;
    }

    public void rx1(float memorize) {
        rx1 = memorize;
    }

    public void dx0(int memorize) {
        dx0 = memorize;
    }

    public void dx1(int memorize) {
        dx1 = memorize;
    }

    public void channel(int select) {
        channel = select;
    }

    public void send(int data) {
        send = data;
    }

}
