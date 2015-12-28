package com.github.drxaos.robo3d.graphics.filters;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.optimize.GeometryBatchFactory;

import java.util.ArrayList;

public class SelectionAppState extends AbstractAppState {

    public enum Type {
        HOVER, SELECT;
    }

    protected Application app;
    protected AssetManager assetManager;
    protected final Node scene;
    protected Node hoverNode, selectNode;
    protected ViewPort viewPort;
    protected RenderManager rm;
    public Material HOVER_COLOR, SELECT_COLOR;

    public SelectionAppState(Node scene) {
        this.scene = scene;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        this.rm = app.getRenderManager();
        this.assetManager = app.getAssetManager();
        setupMaterials(app);
        scene.setCullHint(Spatial.CullHint.Never);
        viewPort = rm.createMainView("Selection Overlay", app.getCamera());
        viewPort.setClearFlags(false, true, false);
        viewPort.attachScene(scene);

        hoverNode = new Node("Hover Node");
        scene.attachChild(hoverNode);
        selectNode = new Node("Select Node");
        scene.attachChild(selectNode);
    }

    @Override
    public void cleanup() {
        rm.removeMainView(viewPort);
        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        scene.updateLogicalState(tpf);
        scene.updateGeometricState();
    }

    @Override
    public void render(RenderManager rm) {
        super.render(rm);
        if (viewPort != null) {
            rm.renderScene(scene, viewPort);
        }
    }

    public void highlight(Spatial spatial, Type type) {
        switch (type) {
            case HOVER: {
                highlight(spatial, hoverNode, HOVER_COLOR);
            }
            break;
            case SELECT: {
                highlight(spatial, selectNode, SELECT_COLOR);
            }
            break;
        }
    }

    protected void highlight(Spatial spatial, Node node, Material color) {
        for (Spatial s : node.getChildren()) {
            s.removeFromParent();
        }
        if (spatial != null) {
            spatial = spatial.clone(false);
            node.attachChild(spatial);
            ArrayList<Geometry> geoms = new ArrayList<>();
            GeometryBatchFactory.gatherGeoms(spatial, geoms);
            for (Geometry geom : geoms) {
                geom.setMaterial(color);
                geom.setQueueBucket(RenderQueue.Bucket.Transparent);
            }
        }
    }


    private void setupMaterials(Application app) {
        AssetManager manager = app.getAssetManager();
        HOVER_COLOR = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        HOVER_COLOR.getAdditionalRenderState().setWireframe(false);
        HOVER_COLOR.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        HOVER_COLOR.setColor("Color", new ColorRGBA(1.0f, 0.3f, 0.3f, 0.2f));
        SELECT_COLOR = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        SELECT_COLOR.getAdditionalRenderState().setWireframe(false);
        SELECT_COLOR.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        SELECT_COLOR.setColor("Color", new ColorRGBA(1.0f, 0.1f, 0.1f, 0.5f));
    }
}
