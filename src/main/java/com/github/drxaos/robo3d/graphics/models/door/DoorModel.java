package com.github.drxaos.robo3d.graphics.models.door;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

public class DoorModel extends ObjectModel {

    public DoorModel(AssetManager am, String objectName) {
        this(am, null, objectName);
    }

    public DoorModel(AssetManager am, String subname, String objectName) {
        super(am, "Models/door/door.blend", subname, objectName);
    }

    public void init(Env env) {
        CollisionShape shape = boundsToCollisionShape();
        physic = new RigidBodyControl(shape, 0);
        this.addControl(physic);
        env.getApp().getBulletAppState().getPhysicsSpace().add(physic);

        //Optimizer.optimize(this, true);
    }
}
