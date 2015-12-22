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

    public float turret() {
        return data.turret();
    }

    public float distance() {
        return data.distance();
    }

    public float type() {
        return data.type();
    }

    public float state() {
        return data.state();
    }
}
