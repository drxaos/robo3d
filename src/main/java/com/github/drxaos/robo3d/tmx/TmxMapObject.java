package com.github.drxaos.robo3d.tmx;

public class TmxMapObject extends TmxEntity {
    public final String name;
    public final int gid;
    public final float x;
    public final float y;
    public final float rotation;
    public final int width;
    public final int height;
    public final String type;
    public TmxPolyline polyline;

    public TmxMapObject(String name, int gid, float x, float y, float rotation,
                        int width, int height, String type) {
        this.name = name;
        this.gid = gid;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void setPolyline(TmxPolyline polyline) {
        this.polyline = polyline;
    }
}
