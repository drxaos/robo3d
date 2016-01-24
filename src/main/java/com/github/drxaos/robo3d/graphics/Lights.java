package com.github.drxaos.robo3d.graphics;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.*;
import com.jme3.post.ssao.SSAOFilter;
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
    SSAOFilter ssao;
    FilterPostProcessor fppa;
    private LightScatteringFilter sunLightFilter;
    private Vector3f lightDir = new Vector3f(-0.39f, -0.32f - 0.5f, -0.74f);

    public void setLights(final Env env) {

        final AssetManager assetManager = env.getAssetManager();
        final Node sceneNode = env.getApp().getSceneNode();
        final ViewPort viewPort = env.getViewPort();

        // Lights
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.7f));
        sun.setDirection(lightDir);
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
        fppa = new FilterPostProcessor(assetManager);
        fppa.setNumSamples(1);

        // Sun Light
        //sunLightFilter = new LightScatteringFilter(lightDir.mult(-3000));
        //sunLightFilter.setNbSamples(8);
        //fppa.addFilter(sunLightFilter);

        // Light beams
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setBloomIntensity(4);
//        bloom.setBlurScale(2);
//        bloom.setDownSamplingFactor(1);
        fppa.addFilter(bloom);

        // Fade in-out
        fade = new FadeFilter(2);
        fppa.addFilter(fade);

        // SSAO
//        ssao = new SSAOFilter(1f, 5f, 0.1f, 0.01f);
//        ssao.setUseOnlyAo(true);
//        fppa.addFilter(ssao);

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
