package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.map.Optimizer;
import com.jme3.asset.AssetManager;

public class RobotModel extends ObjectModel {

    public RobotModel(AssetManager am) {
        super(am, "Models/robot/robot.blend", null);
    }

    public RobotModel(AssetManager am, String subname) {
        super(am, "Models/robot/robot.blend", subname);
    }

    @Override
    protected void prepare() {
        super.prepare();
        Optimizer.optimize(this, true);
    }
}
