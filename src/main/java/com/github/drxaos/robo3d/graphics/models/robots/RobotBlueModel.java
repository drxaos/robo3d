package com.github.drxaos.robo3d.graphics.models.robots;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.github.drxaos.robo3d.tmx.TmxMapObject;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;

import java.util.List;

public class RobotBlueModel extends RobotModel {

    public RobotBlueModel(AssetManager am, String objectName) {
        super(am, "blue", objectName);
    }

    @Override
    protected void prepare() {
        super.prepare();

        List<Material> greenMats = JmeUtils.findMaterials(this, "GreenMat");
        Texture redTexture = am.loadTexture("Models/robot/blue.png");
        for (Material greenMat : greenMats) {
            greenMat.setParam("DiffuseMap", VarType.Texture2D, redTexture);
        }

        List<Material> ammoMats = JmeUtils.findMaterials(this, "AmmoMat");
        Texture redAmmoTexture = am.loadTexture("Models/robot/ammo-blue.png");
        for (Material ammoMat : ammoMats) {
            ammoMat.setParam("DiffuseMap", VarType.Texture2D, redAmmoTexture);
        }

        List<Material> trackMats = JmeUtils.findMaterials(this, "TrackMat");
        Texture redTrackTexture = am.loadTexture("Models/robot/track-blue.png");
        for (Material trackMat : trackMats) {
            trackMat.setParam("DiffuseMap", VarType.Texture2D, redTrackTexture);
        }
    }

    @Override
    public void init(Env env, TmxMapObject mapObject) {
        super.init(env, mapObject);
        glowColorMax = ColorRGBA.Blue;
    }
}
