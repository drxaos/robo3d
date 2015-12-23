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

    public void scan(float angle) {
        data.scan(angle);
    }

    public void fire() {
        data.fire();
    }

    public void rx0(float memorize) {
        data.rx0(memorize);
    }

    public void rx1(float memorize) {
        data.rx1(memorize);
    }

    public void dx0(int memorize) {
        data.dx0(memorize);
    }

    public void dx1(int memorize) {
        data.dx1(memorize);
    }
}
