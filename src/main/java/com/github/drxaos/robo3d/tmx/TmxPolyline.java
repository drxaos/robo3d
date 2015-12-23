package com.github.drxaos.robo3d.tmx;

import java.util.List;

public class TmxPolyline extends TmxEntity {
    public final List<TmxPoint> points;

    public TmxPolyline(List<TmxPoint> points) {
        this.points = points;
    }
}
