package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.map.Optimizer;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.math.Vector3f;

public class RobotModel extends ObjectModel {

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
                if (evt.isPressed() && evt.getKeyCode() == 39) {
                    bl = true;
                }
                if (evt.isPressed() && evt.getKeyCode() == 40) {
                    br = true;
                }
                if (evt.isReleased() && evt.getKeyCode() == 39) {
                    bl = false;
                }
                if (evt.isReleased() && evt.getKeyCode() == 40) {
                    br = false;
                }
            }

            @Override
            public void onTouchEvent(TouchEvent evt) {

            }
        });
    }

    float maxChassisForce = 12f;
    boolean r, l, br, bl;

    public void update(Env env) {
        Vector3f force = this.getWorldRotation().mult(Vector3f.UNIT_X.mult(-maxChassisForce));
        force.setY(0);
        Vector3f bforce = force.mult(-1);
        Vector3f back = this.getWorldRotation().mult(Vector3f.UNIT_X.mult(0.5f));
        Vector3f lc = this.getWorldRotation().mult(Vector3f.UNIT_Z.mult(0.9f)).add(back);
        lc.setY(0);
        Vector3f rc = this.getWorldRotation().mult(Vector3f.UNIT_Z.mult(-0.9f)).add(back);
        rc.setY(0);

        if (l && selected) {
            physic.applyForce(force, lc);
        }
        if (r && selected) {
            physic.applyForce(force, rc);
        }
        if (bl && selected) {
            physic.applyForce(bforce, lc);
        }
        if (br && selected) {
            physic.applyForce(bforce, rc);
        }

        // fix stuck in walls
        if ((r || l || br || bl) && physic.getLinearVelocity().length() < 0.1) {
            physic.setPhysicsLocation(physic.getPhysicsLocation().setY(0f));
        }
    }
}
