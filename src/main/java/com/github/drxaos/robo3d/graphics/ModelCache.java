package com.github.drxaos.robo3d.graphics;

import com.jme3.material.Material;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Map;

public class ModelCache {

    private Map<String, Spatial> mModels;
    private Map<String, Material> mMaterials;

    private ModelCache() {
        mModels = new HashMap<String, Spatial>();
        mMaterials = new HashMap<String, Material>();
    }

    private final static ModelCache INSTANCE = new ModelCache();

    public static ModelCache getInstance() {
        return INSTANCE;
    }

    public final Spatial getModel(final String path) {
        return mModels.get(path);
    }

    public void putModel(final String path, final Spatial model) {
        mModels.put(path, model);
    }

    public Material getMaterial(final String key) {
        return mMaterials.get(key);
    }

    public void putMaterial(final String key, final Material mat) {
        mMaterials.put(key, mat);
    }
}
