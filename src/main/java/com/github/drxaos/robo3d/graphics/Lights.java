package com.github.drxaos.robo3d.graphics;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

public class Lights {

    public static final int SHADOWMAP_SIZE = 1024;
    DirectionalLightShadowRenderer dlsr;
    DirectionalLightShadowFilter dlsf;
    DirectionalLight sun;

    public void setLights(Environment environment) {

        AssetManager assetManager = environment.getAssetManager();
        Node sceneNode = environment.getRenderer().getSceneNode();
        ViewPort viewPort = environment.getViewPort();

        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.7f));
        sun.setDirection(new Vector3f(-1, -1, -1));
        sceneNode.addLight(sun);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.7f));
        sceneNode.addLight(al);

        dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.6f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        //dlsr.displayDebug();
        //dlsr.displayFrustum();
        viewPort.addProcessor(dlsr);

        dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setLambda(0.55f);
        dlsf.setShadowIntensity(0.6f);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        dlsf.setEnabled(false);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);

        viewPort.addProcessor(fpp);
    }
}
