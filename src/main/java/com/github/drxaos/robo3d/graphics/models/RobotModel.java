package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.github.drxaos.robo3d.graphics.map.Optimizer;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import java.util.List;

public class RobotModel extends ObjectModel {
    RigidBodyControl physic;

    public RobotModel(AssetManager am, String objectName) {
        this(am, null, objectName);
    }

    public RobotModel(AssetManager am, String subname, String objectName) {
        super(am, "Models/robot/robot.blend", subname, objectName);
    }

    @Override
    protected void prepare() {
        super.prepare();
    }

    public void init(Env env) {
        CollisionShape shape = boundsToCollisionShape();
        physic = new RigidBodyControl(shape, 1.0f);
        this.addControl(physic);
        env.getApp().getBulletAppState().getPhysicsSpace().add(physic);
        physic.setFriction(0.2f);
        physic.setLinearDamping(0.999f);
        physic.setAngularDamping(0.999f);

        List<Geometry> pickers = JmeUtils.findGeometryByMaterial(this, "Picker");
        for (Geometry picker : pickers) {
            picker.setCullHint(CullHint.Always);
        }

        Optimizer.optimize(this, true);

        env.getApp().getInputManager().addRawInputListener(new RawInputListener() {
            @Override
            public void beginInput() {

            }

            @Override
            public void endInput() {

            }

            @Override
            public void onJoyAxisEvent(JoyAxisEvent evt) {

            }

            @Override
            public void onJoyButtonEvent(JoyButtonEvent evt) {

            }

            @Override
            public void onMouseMotionEvent(MouseMotionEvent evt) {

            }

            @Override
            public void onMouseButtonEvent(MouseButtonEvent evt) {

            }

            @Override
            public void onKeyEvent(KeyInputEvent evt) {
                if (evt.isPressed() && evt.getKeyCode() == 26) {
                    l = true;
                }
                if (evt.isPressed() && evt.getKeyCode() == 27) {
                    r = true;
                }
                if (evt.isReleased() && evt.getKeyCode() == 26) {
                    l = false;
                }
                if (evt.isReleased() && evt.getKeyCode() == 27) {
                    r = false;
                }
            }

            @Override
            public void onTouchEvent(TouchEvent evt) {

            }
        });
    }

    float maxChassisForce = 12f;
    boolean r, l;

    public void update(Env env) {
        Vector3f force = this.getWorldRotation().mult(Vector3f.UNIT_X.mult(-maxChassisForce));
        Vector3f back = this.getWorldRotation().mult(Vector3f.UNIT_X.mult(0.5f));
        Vector3f lc = this.getWorldRotation().mult(Vector3f.UNIT_Z.mult(0.9f)).add(back);
        Vector3f rc = this.getWorldRotation().mult(Vector3f.UNIT_Z.mult(-0.9f)).add(back);
        if (l) {
            physic.applyForce(force, lc);
        }
        if (r) {
            physic.applyForce(force, rc);
        }
    }
}
