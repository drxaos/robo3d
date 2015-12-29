package com.github.drxaos.robo3d.graphics;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessorFix;
import com.jme3.post.filters.ColorOverlayFilter;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

import java.util.concurrent.Callable;

public class Lights {

    public static final int SHADOWMAP_SIZE = 2048;
    DirectionalLightShadowRenderer dlsr;
    DirectionalLight sun;
    AmbientLight al;
    FadeFilter fade;
    ColorOverlayFilter overlay;
    ColorRGBA overlayColor;
    FXAAFilter fxaa;
    FilterPostProcessorFix fppa;

    public void setLights(final Env env) {

        final AssetManager assetManager = env.getAssetManager();
        final Node sceneNode = env.getApp().getSceneNode();
        final ViewPort viewPort = env.getViewPort();

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

        loadFilters(env, assetManager, viewPort);
    }

    protected void loadFilters(final Env env, final AssetManager assetManager, final ViewPort viewPort) {
        // fix black screen on resize
        fppa = new FilterPostProcessorFix(assetManager) {
            boolean loaded = false;

            @Override
            public void reshape(ViewPort vp, int w, int h) {
                if (loaded) {
                    env.getApp().enqueue(new Callable<Void>() {
                        public Void call() {
                            if (fppa != null) {
                                viewPort.removeProcessor(fppa);
                                fppa = null;
                            }
                            loadFilters(env, assetManager, viewPort);
                            return null;
                        }
                    });
                    setDisableReshape(true);
                }
                super.reshape(vp, w, h);
                loaded = true;
            }
        };
        fppa.setNumSamples(4);

        // Fade in-out
        fade = new FadeFilter(2);
        fppa.addFilter(fade);

        // Color
        overlayColor = new ColorRGBA(1f, 0.9f, 0.9f, 1f);
        overlay = new ColorOverlayFilter(overlayColor);
        fppa.addFilter(overlay);

        // Anti-aliasing
        fxaa = new FXAAFilter();
        fxaa.setEnabled(true);
        fppa.addFilter(fxaa);

        viewPort.addProcessor(fppa);
    }
}
