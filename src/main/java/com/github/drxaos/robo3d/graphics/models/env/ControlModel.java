package com.github.drxaos.robo3d.graphics.models.env;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.map.Optimizer;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

public class ControlModel extends ObjectModel {
    public ControlModel(AssetManager am, String objectName) {
        this(am, null, objectName);
    }

    public ControlModel(AssetManager am, String subname, String objectName) {
        super(am, "Models/env/control.blend", subname, objectName);
    }

    @Override
    protected void prepare() {
        super.prepare();
    }

    public void init(Env env) {
        CollisionShape shape = boundsToCollisionShape();
        physic = new RigidBodyControl(shape, 0);
        this.addControl(physic);
        env.getApp().getBulletAppState().getPhysicsSpace().add(physic);

        Optimizer.optimize(this, true);
    }
}
