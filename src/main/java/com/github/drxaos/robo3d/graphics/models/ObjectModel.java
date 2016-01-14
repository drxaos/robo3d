package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.github.drxaos.robo3d.graphics.models.barrels.BarrelBlueModel;
import com.github.drxaos.robo3d.graphics.models.barrels.BarrelGreenModel;
import com.github.drxaos.robo3d.graphics.models.barrels.BarrelModel;
import com.github.drxaos.robo3d.graphics.models.barrels.BarrelRedModel;
import com.github.drxaos.robo3d.graphics.models.box.BoxAmmoModel;
import com.github.drxaos.robo3d.graphics.models.box.BoxFragModel;
import com.github.drxaos.robo3d.graphics.models.box.BoxMedModel;
import com.github.drxaos.robo3d.graphics.models.box.BoxModel;
import com.github.drxaos.robo3d.graphics.models.env.ControlModel;
import com.github.drxaos.robo3d.graphics.models.env.FenceModel;
import com.github.drxaos.robo3d.graphics.models.env.RadioStationModel;
import com.github.drxaos.robo3d.graphics.models.robots.RedRobotModel;
import com.github.drxaos.robo3d.graphics.models.robots.RobotModel;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class ObjectModel extends StaticModel {

    public ObjectModel(AssetManager am, String path, String subname, String objectName) {
        super(am, path, subname, objectName);

        if (fresh) {
            for (Spatial spatial : this.getChildren()) {
                fixLighting(spatial, ElementType.Object);
            }

            prepare();

            applyModel();
        }
    }

    protected void prepare() {
        System.out.println("Preparing: " + meshName + "#" + subname);

        List<Geometry> pickers = JmeUtils.findGeometryByMaterial(this, "Picker");
        for (Geometry picker : pickers) {
            picker.setCullHint(CullHint.Always);
        }

        List<Geometry> bounds = JmeUtils.findGeometryByMaterial(this, "Bound");
        for (Geometry bound : bounds) {
            bound.setCullHint(CullHint.Always);
        }
    }

    public static final Map<String, Class<? extends ObjectModel>> TYPES = new HashMap<String, Class<? extends ObjectModel>>() {{
        put("Robot", RobotModel.class);
        put("RedRobot", RedRobotModel.class);
        put("RadioStation", RadioStationModel.class);
        put("Fence", FenceModel.class);
        put("Control", ControlModel.class);
        put("Box", BoxModel.class);
        put("BoxMed", BoxMedModel.class);
        put("BoxFrag", BoxFragModel.class);
        put("BoxAmmo", BoxAmmoModel.class);
        put("Barrel", BarrelModel.class);
        put("BarrelBlue", BarrelBlueModel.class);
        put("BarrelGreen", BarrelGreenModel.class);
        put("BarrelRed", BarrelRedModel.class);
    }};
}
