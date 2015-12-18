package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.ModelCache;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

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

    private final static float DEFAULT_SCALE = 1.0f;
    private final static boolean ENABLE_NORMAL_MAP = false;
    private Spatial mModel;
    private Material mMaterial;
    private Node mModelNode;

    public StaticModel(AssetManager am, String meshName) {
        this(am, meshName, null, null);
    }

    public StaticModel(AssetManager am, String meshName,
                       String diffusePath, String normalPath) {
        mModelNode = new Node();
        mModelNode.setShadowMode(ShadowMode.CastAndReceive);

        Spatial model = ModelCache.getInstance().getModel(meshName);
        if (model == null) {
            model = am.loadModel(meshName.replaceFirst("#.+$", ""));
            ModelCache.getInstance().putModel(meshName, model);
        }
        mModel = model.clone(false);
        mModel.setLocalScale(DEFAULT_SCALE);

        if (diffusePath != null) {
            String cacheKey = diffusePath + "::" + normalPath;

            Material cachedMat = ModelCache.getInstance().getMaterial(cacheKey);

            if (cachedMat != null) {
                mMaterial = cachedMat.clone();
            } else {
                mMaterial = new Material(am, "Common/MatDefs/Light/Lighting.j3md");
                Texture diffuseTex = am.loadTexture(diffusePath);
                diffuseTex.setMinFilter(Texture.MinFilter.Trilinear);
                mMaterial.setTexture("DiffuseMap", diffuseTex);

                if (ENABLE_NORMAL_MAP && normalPath != null) {
                    TangentBinormalGenerator.generate(mModel);
                    mMaterial.setTexture("NormalMap", am.loadTexture(normalPath));
                }

                mMaterial.setBoolean("UseMaterialColors", true);
                mMaterial.setColor("Ambient", ColorRGBA.White);
                mMaterial.setColor("Diffuse", ColorRGBA.DarkGray);

                ModelCache.getInstance().putMaterial(cacheKey, mMaterial);
            }

            mModel.setMaterial(mMaterial);
        }

        mModelNode.attachChild(mModel);
        this.attachChild(mModelNode);
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
        }
        if (spatial instanceof Node) {
            for (Spatial child : ((Node) spatial).getChildren()) {
                fixLighting(child, type);
            }
        }
    }

    public Spatial getModel() {
        return mModel;
    }

    public Node getModelNode() {
        return mModelNode;
    }

    public Material getMaterial() {
        return mMaterial;
    }

}
