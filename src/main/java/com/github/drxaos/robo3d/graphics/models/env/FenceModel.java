package com.github.drxaos.robo3d.graphics.models.env;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.map.Optimizer;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.github.drxaos.robo3d.tmx.TmxMapObject;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

public class FenceModel extends ObjectModel {

    public FenceModel(AssetManager am, String objectName) {
        this(am, null, objectName);
    }

    public FenceModel(AssetManager am, String subname, String objectName) {
        super(am, "Models/env/fence.blend", subname, objectName);
    }

    @Override
    protected void prepare() {
        super.prepare();
    }

    public void init(Env env, TmxMapObject mapObject) {
        CollisionShape shape = boundsToCollisionShape();
        physic = new RigidBodyControl(shape, 0.5f);
        this.addControl(physic);
        physic.setLinearDamping(0.9f);
        physic.setAngularDamping(0.9f);
        env.getApp().getBulletAppState().getPhysicsSpace().add(physic);

        Optimizer.optimize(this, true);
    }


    @Override
    public float getMass() {
        return 0.2f;
    }
}
