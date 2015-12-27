package com.github.drxaos.robo3d.graphics.map;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.github.drxaos.robo3d.graphics.models.TileModel;
import com.github.drxaos.robo3d.tmx.*;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class MapLoader {

    public void loadTo(Env env) {
        AssetManager assetManager = env.getAssetManager();
        Node sceneNode = env.getApp().getSceneNode();
        ViewPort viewPort = env.getViewPort();

        Spatial sky = SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false);
        sky.setLocalScale(350);
        sceneNode.attachChild(sky);

        try {
            assetManager.registerLoader(TmxLoader.class, "tmx");
            TmxMap map = (TmxMap) assetManager.loadAsset("Tiles/level1.tmx");


            for (TmxLayer layer : map.getLayers()) {
                Node layerNode = new Node("Layer_" + layer.name);
                for (int x = 0; x < layer.width; x++) {
                    for (int y = 0; y < layer.height; y++) {

                        int r = layer.getTileRotation(x, y);
                        int id = layer.getTileId(x, y);

                        if (id == 0) {
                            continue;
                        }

                        TmxTileset tileset = map.getTilesetByGid(id);

                        TileModel tileModel = TileModel.TYPES.get(tileset.name)
                                .getDeclaredConstructor(AssetManager.class, Integer.class)
                                .newInstance(assetManager, id - tileset.firstGid + 1);
                        tileModel.move(x * 6 + 3, 0, y * 6 + 3);
                        tileModel.rotate(0, JmeUtils.degreesToRad(-r), 0);
                        layerNode.attachChild(tileModel);
                        tileModel.init(env);
                        env.getApp().getObjects().add(tileModel);
                    }
                }
                try {
                    Object staticLayer = TileModel.TYPES.get(layer.name).getDeclaredField("STATIC_LAYER").get(null);
                    if (staticLayer.equals(Boolean.TRUE)) {
                        Optimizer.optimize(layerNode, true);
                    }
                } catch (Exception e) {
                    // non-static
                }
                sceneNode.attachChild(layerNode);
            }
            RigidBodyControl floorPhy = new RigidBodyControl(new PlaneCollisionShape(new Plane(Vector3f.UNIT_Y, 0)), 0.0f);
            env.getApp().getBulletAppState().getPhysicsSpace().add(floorPhy);

            for (TmxObjectGroup tmxObjectGroup : map.getObjectGroups()) {
                for (TmxMapObject object : tmxObjectGroup.getObjects()) {
                    Class<? extends ObjectModel> typeCls = ObjectModel.TYPES.get(object.type);
                    if (typeCls == null) {
                        continue;
                    }
                    ObjectModel objectModel = typeCls
                            .getDeclaredConstructor(AssetManager.class)
                            .newInstance(assetManager);
                    objectModel.move(
                            object.x / 50 * 6 + FastMath.cos(JmeUtils.degreesToRad(object.rotation - 45)) * 3 * FastMath.sqrt(2),
                            0,
                            object.y / 50 * 6 + FastMath.sin(JmeUtils.degreesToRad(object.rotation - 45)) * 3 * FastMath.sqrt(2));
                    objectModel.rotate(0, JmeUtils.degreesToRad(-object.rotation), 0);
                    sceneNode.attachChild(objectModel);
                    objectModel.init(env);
                    env.getApp().getObjects().add(objectModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
