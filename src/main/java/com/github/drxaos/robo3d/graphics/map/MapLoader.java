package com.github.drxaos.robo3d.graphics.map;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.Utils;
import com.github.drxaos.robo3d.graphics.models.*;
import com.github.drxaos.robo3d.tmx.TmxLayer;
import com.github.drxaos.robo3d.tmx.TmxLoader;
import com.github.drxaos.robo3d.tmx.TmxMap;
import com.github.drxaos.robo3d.tmx.TmxTileset;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

import java.util.HashMap;
import java.util.Map;

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
            TmxMap map = (TmxMap) assetManager.loadAsset("Tiles/test.tmx");

            Map<String, Class<? extends TileModel>> tiles = new HashMap<String, Class<? extends TileModel>>();
            tiles.put("roofs", RoofTileModel.class);
            tiles.put("walls", WallTileModel.class);
            tiles.put("doors", DoorTileModel.class);
            tiles.put("roads", RoadTileModel.class);

            for (TmxLayer layer : map.getLayers()) {

                for (int x = 0; x < layer.width; x++) {
                    for (int y = 0; y < layer.height; y++) {
                        int r = layer.getTileRotation(x, y);
                        int id = layer.getTileId(x, y);

                        if (id == 0) {
                            continue;
                        }

                        TmxTileset tileset = map.getTilesetByGid(id);

                        TileModel wallTileModel = tiles.get(tileset.name).getDeclaredConstructor(AssetManager.class, Integer.class).newInstance(assetManager, id - tileset.firstGid + 1);
                        wallTileModel.move(x * 6, 0, y * 6);
                        wallTileModel.rotate(0, Utils.degreesToRad(-r), 0);
                        sceneNode.attachChild(wallTileModel);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
