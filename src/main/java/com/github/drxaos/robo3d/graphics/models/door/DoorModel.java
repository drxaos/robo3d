package com.github.drxaos.robo3d.graphics.models.door;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.github.drxaos.robo3d.tmx.TmxMapObject;
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

    public void init(Env env, TmxMapObject mapObject) {
        CollisionShape shape = boundsToCollisionShape();
        physic = new RigidBodyControl(shape, 1);
        physic.setKinematic(true);
        this.addControl(physic);
        env.getApp().getBulletAppState().getPhysicsSpace().add(physic);

        //Optimizer.optimize(this, true);
    }

    int action = 1;
    final static int ACTION_IDLE = 0;
    final static int ACTION_CLOSING = 1;
    final static int ACTION_OPENING = -1;
    float state = 0;
    float speed = 0.1f;
    final static float STATE_OPEN = -3.85f;
    final static float STATE_CLOSE = 0;

    public boolean isOpen() {
        return state <= STATE_OPEN;
    }

    @Override
    public void update(Env env) {
        super.update(env);

        state += speed * action;

        if (action == ACTION_CLOSING && state > STATE_CLOSE) {
            action = ACTION_IDLE;
            state = STATE_CLOSE;
        }
        if (action == ACTION_OPENING && state < STATE_OPEN) {
            action = ACTION_IDLE;
            state = STATE_OPEN;
        }

        setLocalTranslation(getLocalTranslation().setY(state));
    }

    final static String OPEN = "open";
    final static String CLOSE = "close";

    public void signal(String name) {
        if (OPEN.equals(name)) {
            action = ACTION_OPENING;
        }
        if (CLOSE.equals(name)) {
            action = ACTION_CLOSING;
        }
    }
}
