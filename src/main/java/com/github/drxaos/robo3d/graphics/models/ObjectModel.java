package com.github.drxaos.robo3d.graphics.models;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Map;

abstract public class ObjectModel extends StaticModel {

    public ObjectModel(AssetManager am, String path) {
        super(am, path);
        for (Spatial spatial : this.getChildren()) {
            fixLighting(spatial, ElementType.Object);
        }
    }

    public static final Map<String, Class<? extends ObjectModel>> TYPES = new HashMap<String, Class<? extends ObjectModel>>() {{
        put("Robot", RobotModel.class);
        put("RedRobot", RedRobotModel.class);
    }};
}
