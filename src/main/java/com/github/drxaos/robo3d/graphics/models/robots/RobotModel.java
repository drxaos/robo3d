package com.github.drxaos.robo3d.graphics.models.robots;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.github.drxaos.robo3d.graphics.map.Optimizer;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.github.drxaos.robo3d.tmx.TmxMapObject;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class RobotModel extends ObjectModel {

    public RobotModel(AssetManager am, String objectName) {
        this(am, null, objectName);
    }

    public RobotModel(AssetManager am, String subname, String objectName) {
        super(am, "Models/robot/robot.blend", subname, objectName);
    }

    protected ColorRGBA glowColorMax;
    protected ColorRGBA glowColor;

    @Override
    protected void prepare() {
        super.prepare();

        glowColorMax = ColorRGBA.Green;
        glowColor = new ColorRGBA();
        for (Material glow : JmeUtils.findMaterials(this, "Glow")) {
            glow.setBoolean("UseMaterialColors", true);
            glow.setColor("Diffuse", glowColor);
            glow.setColor("Ambient", glowColor);
            glow.setColor("GlowColor", glowColor);
        }
    }

    public void init(Env env, TmxMapObject mapObject) {
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
        super.update(env);

        float glowScale = FastMath.abs(FastMath.sin(0.1f * env.getApp().getFrame())) * 0.2f + 0.8f;
        glowColor.set(glowColorMax).multLocal(glowScale);

        Vector3f force = this.getWorldRotation().mult(Vector3f.UNIT_X.mult(-1));
        force.setY(0).normalizeLocal().multLocal(maxChassisForce);
        Vector3f bforce = force.mult(-1);
        Vector3f back = this.getWorldRotation().mult(Vector3f.UNIT_X.mult(0.5f));
        Vector3f lc = this.getWorldRotation().mult(Vector3f.UNIT_Z.mult(0.9f)).setY(0.5f);
        Vector3f rc = this.getWorldRotation().mult(Vector3f.UNIT_Z.mult(-0.9f)).setY(0.5f);

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
//        if ((r || l || br || bl) && physic.getLinearVelocity().length() < 0.1) {
//            physic.setPhysicsLocation(physic.getPhysicsLocation().setY(0f));
//        }
    }
}
