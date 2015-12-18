package com.github.drxaos.robo3d.graphics.map;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.github.drxaos.robo3d.graphics.models.TileModel;
import com.github.drxaos.robo3d.tmx.*;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
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
                        sceneNode.attachChild(tileModel);
                    }
                }

            }

            for (TmxObjectGroup tmxObjectGroup : map.getObjectGroups()) {
                for (TmxMapObject object : tmxObjectGroup.getObjects()) {
                    ObjectModel objectModel = ObjectModel.TYPES.get(object.type)
                            .getDeclaredConstructor(AssetManager.class)
                            .newInstance(assetManager);
                    objectModel.move(
                            object.x / 50 * 6 + FastMath.cos(JmeUtils.degreesToRad(object.rotation - 45)) * 3 * FastMath.sqrt(2),
                            0,
                            object.y / 50 * 6 + FastMath.sin(JmeUtils.degreesToRad(object.rotation - 45)) * 3 * FastMath.sqrt(2));
                    objectModel.rotate(0, JmeUtils.degreesToRad(-object.rotation), 0);
                    sceneNode.attachChild(objectModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
