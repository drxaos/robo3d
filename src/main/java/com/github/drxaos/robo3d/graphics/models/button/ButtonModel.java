package com.github.drxaos.robo3d.graphics.models.button;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.github.drxaos.robo3d.tmx.TmxMapObject;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonModel extends ObjectModel {

    public ButtonModel(AssetManager am, String objectName) {
        this(am, null, objectName);
    }

    public ButtonModel(AssetManager am, String subname, String objectName) {
        super(am, "Models/button/button.blend", subname, objectName);
    }

    GhostControl ghost;
    Map<String, String> on = new HashMap<>(), off = new HashMap<>();

    public void init(Env env, TmxMapObject mapObject) {
        List<Geometry> bounds = JmeUtils.findGeometryByMaterial(this, "ButtonMat");
        CollisionShape shape = CollisionShapeFactory.createDynamicMeshShape(bounds.get(0));
        ghost = new GhostControl(shape);
        this.addControl(ghost);
        env.getApp().getBulletAppState().getPhysicsSpace().add(ghost);

        readSignals(mapObject.getProperty("on_signals"), on);
        readSignals(mapObject.getProperty("off_signals"), off);

        //Optimizer.optimize(this, true);
    }

    protected void readSignals(String value, Map<String, String> map) {
        if (value != null && !value.isEmpty()) {
            for (String s : value.split(",")) {
                String[] split = s.split("=");
                map.put(split[0], split[1]);
            }
        }
    }

    protected ColorRGBA glowColorMax;
    protected ColorRGBA glowColor;

    @Override
    protected void prepare() {
        super.prepare();

        glowColorMax = ColorRGBA.Yellow;
        glowColor = new ColorRGBA();
        for (Material glow : JmeUtils.findMaterials(this, "Glow")) {
            glow.setBoolean("UseMaterialColors", true);
            glow.setColor("Diffuse", glowColor);
            glow.setColor("Ambient", glowColor);
            glow.setColor("GlowColor", glowColor);
            glow.getAdditionalRenderState().setPolyOffset(-1, -1);
        }

        for (Geometry geometry : JmeUtils.findGeometryByMaterial(this, null)) {
            geometry.setShadowMode(RenderQueue.ShadowMode.Receive);
        }
    }

    protected float SIZE = 1.5f;
    protected float activationMass = 1.0f;
    protected boolean active = false;
    protected boolean sent = false;

    @Override
    public void update(Env env) {
        super.update(env);

        float glowScale = FastMath.abs(FastMath.sin(0.1f * env.getApp().getFrame())) * 0.2f + 0.4f;
        glowColor.set(glowColorMax).multLocal(glowScale);

        float mass = 0;
        for (PhysicsCollisionObject object : ghost.getOverlappingObjects()) {
            if (object instanceof RigidBodyControl) {
                Vector3f objLoc = ((RigidBodyControl) object).getPhysicsLocation();
                if (ghost.getPhysicsLocation().distance(objLoc) < SIZE) {
                    Object spatial = object.getUserObject();
                    if (spatial instanceof ObjectModel) {
                        mass += ((ObjectModel) spatial).getMass();
                    }
                }
            }
        }
        if (!active && mass >= activationMass) {
            active = true;
            glowColorMax = ColorRGBA.Green;
        }
        if (active && mass < activationMass) {
            active = false;
            glowColorMax = ColorRGBA.Yellow;
        }
    }

    @Override
    public Map<String, String> getSignals() {
        if (sent != active) {
            sent = active;
            return active ? on : off;
        }
        return null;
    }
}
