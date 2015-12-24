package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.ModelCache;
import com.jme3.asset.AssetManager;
import com.jme3.material.MatParamTexture;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LodControl;
import com.jme3.texture.Texture;
import jme3tools.optimize.LodGenerator;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class StaticModel extends Node {

    enum ElementType {
        Floor(ShadowMode.Receive),
        Wall(ShadowMode.CastAndReceive),
        Ceil(ShadowMode.CastAndReceive),
        Door(ShadowMode.CastAndReceive),
        Object(ShadowMode.CastAndReceive),
        Display(ShadowMode.Off),
        Camera(ShadowMode.Off),
        None(ShadowMode.Off);

        ShadowMode shadowMode;

        ElementType(ShadowMode shadowMode) {
            this.shadowMode = shadowMode;
        }

        public ShadowMode getShadowMode() {
            return shadowMode;
        }
    }

    protected AssetManager am;
    protected boolean fresh = false;
    protected String meshName, subname;
    protected Body physicsBody;

    public StaticModel(AssetManager am, String meshName, String subname) {
        this.am = am;
        this.meshName = meshName;
        this.subname = subname;

        Spatial model = ModelCache.getInstance().getModel(subname == null ? meshName : meshName + "#" + subname);
        if (model == null) {
            model = am.loadModel(meshName);
            fresh = true;
        }
        model = model.clone(false);
        for (Spatial spatial : ((Node) model).getChildren()) {
            this.attachChild(spatial);
        }
    }

    protected void applyModel() {
        ModelCache.getInstance().putModel(subname == null ? meshName : meshName + "#" + subname, this);
        fresh = false;
    }

    protected void fixLighting(Spatial spatial, ElementType type) {
        if (spatial instanceof Geometry) {
            spatial.setShadowMode(type.getShadowMode());
            Material material = ((Geometry) spatial).getMaterial();
            material.setBoolean("UseMaterialColors", true);
            material.setColor("Ambient", ColorRGBA.White.mult(1.0f));
            material.setColor("Diffuse", ColorRGBA.White.clone());
            material.setColor("Specular", ColorRGBA.White.mult(0.1f));
            material.setFloat("Shininess", 0.1f);

            MatParamTexture diffuseMap = material.getTextureParam("DiffuseMap");
            if (diffuseMap != null) {
                diffuseMap.getTextureValue().setMinFilter(Texture.MinFilter.Trilinear);
                diffuseMap.getTextureValue().setMagFilter(Texture.MagFilter.Bilinear);
            }

            LodGenerator lod = new LodGenerator((Geometry) spatial);
            lod.bakeLods(LodGenerator.TriangleReductionMethod.PROPORTIONAL, 0.25f, 0.5f, 0.75f, 0.9f);
            LodControl lc = new LodControl();
            lc.setDistTolerance(0.0005f);
            spatial.addControl(lc);
        }
        if (spatial instanceof Node) {
            for (Spatial child : ((Node) spatial).getChildren()) {
                fixLighting(child, type);
            }
        }
    }

    public final Body addBodyTo(World world) {
        return physicsBody = createBody(world);
    }

    protected Body createBody(World world) {
        return null;
    }
}
