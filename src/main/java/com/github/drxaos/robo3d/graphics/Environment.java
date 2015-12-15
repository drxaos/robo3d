package com.github.drxaos.robo3d.graphics;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;

public class Environment {

    private Game3DRenderer renderer;
    private AssetManager assetManager;
    private ViewPort viewPort;
    private Camera cam;

    public Environment(Game3DRenderer renderer, AssetManager assetManager, ViewPort viewPort, Camera cam) {
        this.renderer = renderer;
        this.assetManager = assetManager;
        this.viewPort = viewPort;
        this.cam = cam;
    }

    public Game3DRenderer getRenderer() {
        return renderer;
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
