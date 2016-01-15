package com.github.drxaos.robo3d.graphics.models.barrel;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;

import java.util.List;

public class BarrelModel extends ObjectModel {

    public BarrelModel(AssetManager am, String objectName) {
        this(am, null, objectName);
    }

    public BarrelModel(AssetManager am, String subname, String objectName) {
        super(am, "Models/barrel/barrel.blend", subname, objectName);
    }

    @Override
    protected void prepare() {
        super.prepare();

        if (subname != null) {
            List<Material> mat = JmeUtils.findMaterials(this, "BarrelMat");
            Texture tex = am.loadTexture("Models/barrel/" + subname + ".jpg");
            for (Material material : mat) {
                material.setParam("DiffuseMap", VarType.Texture2D, tex);
            }
        }
    }

    public void init(Env env) {
        CollisionShape shape = CollisionShapeFactory.createDynamicMeshShape(this);
        physic = new RigidBodyControl(shape, 0.5f);
        this.addControl(physic);
        physic.setLinearDamping(0.9f);
        physic.setAngularDamping(0.9f);
        env.getApp().getBulletAppState().getPhysicsSpace().add(physic);

        //Optimizer.optimize(this, true);
    }
}
