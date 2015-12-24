package com.github.drxaos.robo3d.graphics;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.ColorOverlayFilter;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

public class Lights {

    public static final int SHADOWMAP_SIZE = 2048;
    DirectionalLightShadowRenderer dlsr;
    DirectionalLight sun;
    AmbientLight al;
    FadeFilter fade;
    ColorOverlayFilter overlay;
    ColorRGBA overlayColor;
    FXAAFilter fxaa;

    public void setLights(Env env) {

        AssetManager assetManager = env.getAssetManager();
        Node sceneNode = env.getApp().getSceneNode();
        ViewPort viewPort = env.getViewPort();

        // Lights
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.7f));
        sun.setDirection(new Vector3f(-0.5f, -1, -1));
        sceneNode.addLight(sun);

        al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.7f));
        sceneNode.addLight(al);

        // Shadows
        dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 4);
        dlsr.setLight(sun);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.6f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        viewPort.addProcessor(dlsr);

        // Filters
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.setNumSamples(2);

        // Fade in-out
        fade = new FadeFilter(2);
        fpp.addFilter(fade);

        // Color
        overlayColor = new ColorRGBA(1f, 0.9f, 0.9f, 1f);
        overlay = new ColorOverlayFilter(overlayColor);
        fpp.addFilter(overlay);

        // Anti-aliasing
        fxaa = new FXAAFilter();
        fxaa.setSubPixelShift(0f);
        fxaa.setReduceMul(0f);
        fxaa.setVxOffset(5.0f);
        fxaa.setEnabled(true);
        fpp.addFilter(fxaa);

        viewPort.addProcessor(fpp);
    }
}
