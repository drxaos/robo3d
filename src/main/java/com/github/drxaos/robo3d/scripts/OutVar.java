package com.github.drxaos.robo3d.scripts;

public class OutVar {
    Data data;

    void setData(Data data) {
        this.data = data;
    }

    public void chassis_left(float max100) {
        data.chassis_left(max100);
    }

    public void chassis_right(float max100) {
        data.chassis_right(max100);
    }

    public void state(float memorize) {
        data.state(memorize);
    }

    public void scan(float angle) {
        data.scan(angle);
    }

    public void fire() {
        data.fire();
    }
}
