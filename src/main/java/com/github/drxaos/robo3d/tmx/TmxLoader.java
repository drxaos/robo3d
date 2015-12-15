package com.github.drxaos.robo3d.tmx;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;

import java.io.IOException;

public class TmxLoader implements AssetLoader {
    public Object load(AssetInfo assetInfo) throws IOException {
        return new TmxMap(assetInfo);
    }
}
