package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;

import java.util.List;

public class RedRobotModel extends RobotModel {

    public RedRobotModel(AssetManager am) {
        super(am, "red");
    }

    @Override
    protected void prepare() {
        super.prepare();

        List<Material> greenMats = JmeUtils.findMaterials(this, "GreenMat");
        Texture redTexture = am.loadTexture("Models/robot/red.png");
        for (Material greenMat : greenMats) {
            greenMat.setParam("DiffuseMap", VarType.Texture2D, redTexture);
        }

        List<Material> ammoMats = JmeUtils.findMaterials(this, "AmmoMat");
        Texture redAmmoTexture = am.loadTexture("Models/robot/ammo-red.png");
        for (Material ammoMat : ammoMats) {
            ammoMat.setParam("DiffuseMap", VarType.Texture2D, redAmmoTexture);
        }

        List<Material> trackMats = JmeUtils.findMaterials(this, "TrackMat");
        Texture redTrackTexture = am.loadTexture("Models/robot/track-red.png");
        for (Material trackMat : trackMats) {
            trackMat.setParam("DiffuseMap", VarType.Texture2D, redTrackTexture);
        }
    }
}
