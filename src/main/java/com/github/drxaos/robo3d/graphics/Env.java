package com.github.drxaos.robo3d.graphics;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;

public class Env {

    private App app;
    private AssetManager assetManager;
    private ViewPort viewPort;
    private Camera cam;

    public Env(App app, AssetManager assetManager, ViewPort viewPort, Camera cam) {
        this.app = app;
        this.assetManager = assetManager;
        this.viewPort = viewPort;
        this.cam = cam;
    }

    public App getApp() {
        return app;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public ViewPort getViewPort() {
        return viewPort;
    }

    public Camera getCam() {
        return cam;
    }
}
