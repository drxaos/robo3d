package com.github.drxaos.robo3d.graphics.gui;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;

public class InfoAppState extends AbstractAppState {

    private Application app;
    protected InfoView infoView;
    protected Info info;

    protected Node guiNode;
    protected float secondCounter = 0.0f;
    protected int frameCounter = 0;
    protected BitmapText fpsText;
    protected BitmapFont guiFont;
    protected Geometry darkenFps;
    protected Geometry darkenStats;

    public InfoAppState(Node guiNode, BitmapFont guiFont, Info info) {
        this.guiNode = guiNode;
        this.guiFont = guiFont;
        this.info = info;
    }

    public BitmapText getFpsText() {
        return fpsText;
    }

    public InfoView getInfoView() {
        return infoView;
    }

    public void setGuiFont(BitmapFont guiFont) {
        this.guiFont = guiFont;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;

        if (app instanceof SimpleApplication) {
            SimpleApplication simpleApp = (SimpleApplication) app;
            if (guiNode == null) {
                guiNode = simpleApp.getGuiNode();
            }
        }

        if (guiNode == null) {
            throw new RuntimeException("No guiNode specific and cannot be automatically determined.");
        }

        if (guiFont == null) {
            guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        }

        loadFpsText();
        loadStatsView();
        loadDarken();

        if (fpsText != null) {
            fpsText.setCullHint(CullHint.Never);
            if (darkenFps != null) {
                darkenFps.setCullHint(CullHint.Never);
            }
        }
        if (infoView != null) {
            infoView.setEnabled(true);
            infoView.setCullHint(CullHint.Never);
            if (darkenStats != null) {
                darkenStats.setCullHint(CullHint.Never);
            }
        }
    }

    public void loadFpsText() {
        if (fpsText == null) {
            fpsText = new BitmapText(guiFont, false);
        }

        fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        fpsText.setText("FPS");
        fpsText.setCullHint(CullHint.Never);
        guiNode.attachChild(fpsText);

    }

    public void loadStatsView() {
        infoView = new InfoView("Info View", app.getAssetManager(), info);
        // move it up so it appears above fps text
        infoView.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        infoView.setEnabled(true);
        infoView.setCullHint(CullHint.Never);
        guiNode.attachChild(infoView);
    }

    public void loadDarken() {
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(0, 0, 0, 0.5f));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        darkenFps = new Geometry("StatsDarken", new Quad(200, fpsText.getLineHeight()));
        darkenFps.setMaterial(mat);
        darkenFps.setLocalTranslation(0, 0, -1);
        darkenFps.setCullHint(CullHint.Never);
        guiNode.attachChild(darkenFps);

        darkenStats = new Geometry("StatsDarken", new Quad(200, infoView.getHeight()));
        darkenStats.setMaterial(mat);
        darkenStats.setLocalTranslation(0, fpsText.getHeight(), -1);
        darkenStats.setCullHint(CullHint.Never);
        guiNode.attachChild(darkenStats);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        fpsText.setCullHint(CullHint.Never);
        darkenFps.setCullHint(CullHint.Never);
        infoView.setEnabled(true);
        infoView.setCullHint(CullHint.Never);
        darkenStats.setCullHint(CullHint.Never);
    }

    @Override
    public void update(float tpf) {
        secondCounter += app.getTimer().getTimePerFrame();
        frameCounter++;
        if (secondCounter >= 1.0f) {
            int fps = (int) (frameCounter / secondCounter);
            fpsText.setText("FPS: " + fps);
            secondCounter = 0.0f;
            frameCounter = 0;
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();

        guiNode.detachChild(infoView);
        guiNode.detachChild(fpsText);
        guiNode.detachChild(darkenFps);
        guiNode.detachChild(darkenStats);
    }
}
