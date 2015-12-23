package com.github.drxaos.robo3d.scripts;

public class InVar {

    Data data;

    void setData(Data data) {
        this.data = data;
    }

    public float x() {
        return data.x();
    }

    public float y() {
        return data.y();
    }

    public float angle() {
        return data.angle();
    }

    public float turret_wait() {
        return data.turret();
    }

    public float armor() {
        return data.armor();
    }

    public float scan_distance() {
        return data.distance();
    }

    public int scan_type() {
        return data.type();
    }

    public float rx0() {
        return data.rx0();
    }

    public float rx1() {
        return data.rx1();
    }

    public int dx0() {
        return data.dx0();
    }

    public int dx1() {
        return data.dx1();
    }
}
