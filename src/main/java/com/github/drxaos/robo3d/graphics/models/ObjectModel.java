package com.github.drxaos.robo3d.graphics.models;

import com.jme3.asset.AssetManager;

import java.util.HashMap;
import java.util.Map;

abstract public class ObjectModel extends StaticModel {

    public ObjectModel(AssetManager am, String path) {
        super(am, path);
    }

    public static final Map<String, Class<? extends ObjectModel>> TYPES = new HashMap<String, Class<? extends ObjectModel>>() {{
        put("robot", RobotModel.class);
    }};
}
