package com.github.drxaos.robo3d.graphics.map;

import com.github.drxaos.robo3d.graphics.Environment;
import com.github.drxaos.robo3d.graphics.Utils;
import com.github.drxaos.robo3d.graphics.models.GroundTileModel;
import com.github.drxaos.robo3d.tmx.TmxLayer;
import com.github.drxaos.robo3d.tmx.TmxLoader;
import com.github.drxaos.robo3d.tmx.TmxMap;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class MapLoader {

    public void loadTo(Environment environment) {
        AssetManager assetManager = environment.getAssetManager();
        Node sceneNode = environment.getRenderer().getSceneNode();
        ViewPort viewPort = environment.getViewPort();

        Spatial sky = SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false);
        sky.setLocalScale(350);
        sceneNode.attachChild(sky);


        try {
            assetManager.registerLoader(TmxLoader.class, "tmx");
            TmxMap map = (TmxMap) assetManager.loadAsset("Tiles/test.tmx");

            TmxLayer ground = map.getLayer("Ground");

            for (int x = 0; x < ground.width; x++) {
                for (int y = 0; y < ground.height; y++) {
                    int r = ground.getTileRotation(x, y);
                    int id = ground.getTileId(x, y);

                    GroundTileModel.TileType tileType = GroundTileModel.TILES.get(id);
                    if (tileType == null) {
                        System.out.println("NO TILE " + id);
                        continue;
                    }
                    GroundTileModel groundTileModel = new GroundTileModel(assetManager, tileType);
                    groundTileModel.move(x * 6, 0, y * 6);
                    groundTileModel.rotate(0, Utils.degreesToRad(-r), 0);
                    sceneNode.attachChild(groundTileModel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
