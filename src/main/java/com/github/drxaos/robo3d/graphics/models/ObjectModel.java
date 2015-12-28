package com.github.drxaos.robo3d.graphics.models;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Map;

abstract public class ObjectModel extends StaticModel {

    public ObjectModel(AssetManager am, String path, String subname, String objectName) {
        super(am, path, subname, objectName);

        if (fresh) {
            for (Spatial spatial : this.getChildren()) {
                fixLighting(spatial, ElementType.Object);
            }

            prepare();

            applyModel();
        }
    }

    protected void prepare() {
        System.out.println("Preparing: " + meshName + "#" + subname);
    }

    public static final Map<String, Class<? extends ObjectModel>> TYPES = new HashMap<String, Class<? extends ObjectModel>>() {{
        put("Robot", RobotModel.class);
        put("RedRobot", RedRobotModel.class);
        put("RadioStation", RadioStationModel.class);
        put("Fence", FenceModel.class);
        put("Control", ControlModel.class);
    }};
}
