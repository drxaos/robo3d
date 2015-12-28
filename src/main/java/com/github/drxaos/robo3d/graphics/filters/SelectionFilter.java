package com.github.drxaos.robo3d.graphics.filters;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.Image.Format;

public class SelectionFilter extends Filter {

    private Pass normalPass;
    private float edgeWidth = 1.0f;
    private float edgeIntensity = 1.0f;
    private float normalThreshold = 0.5f;
    private float depthThreshold = 0.1f;
    private float normalSensitivity = 1.0f;
    private float depthSensitivity = 10.0f;
    private ColorRGBA edgeColor = new ColorRGBA(0.5f, 1f, 1f, 0.7f);
    private RenderManager renderManager;
    private ViewPort viewPort;

    public SelectionFilter() {
        super("SelectionFilter");
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    protected void postQueue(RenderQueue queue) {
        Renderer r = renderManager.getRenderer();
        r.setFrameBuffer(normalPass.getRenderFrameBuffer());
        renderManager.getRenderer().clearBuffers(true, true, true);
        renderManager.setForcedTechnique("PreNormalPass");
        renderManager.renderViewPortQueues(viewPort, false);
        renderManager.setForcedTechnique(null);
        renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
    }

    @Override
    protected Material getMaterial() {
        material.setTexture("NormalsTexture", normalPass.getRenderedTexture());
        return material;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        this.renderManager = renderManager;
        this.viewPort = vp;
        normalPass = new Pass();
        normalPass.init(renderManager.getRenderer(), w, h, Format.RGBA8, Format.Depth);
        material = new Material(manager, "Filters/Selection/Selection.j3md");
        material.setFloat("EdgeWidth", edgeWidth);
        material.setFloat("EdgeIntensity", edgeIntensity);
        material.setFloat("NormalThreshold", normalThreshold);
        material.setFloat("DepthThreshold", depthThreshold);
        material.setFloat("NormalSensitivity", normalSensitivity);
        material.setFloat("DepthSensitivity", depthSensitivity);
        material.setColor("EdgeColor", edgeColor);
    }
}
