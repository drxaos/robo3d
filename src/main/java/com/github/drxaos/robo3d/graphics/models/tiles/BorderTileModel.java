package com.github.drxaos.robo3d.graphics.models.tiles;

import com.github.drxaos.robo3d.graphics.Env;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorderTileModel extends TileModel {
    public static final boolean STATIC_LAYER = true;

    public enum Element implements TileModel.Element {
        Border(ElementType.Wall),
        BorderCorner(ElementType.Wall);

        ElementType elementType;

        Element(ElementType elementType) {
            this.elementType = elementType;
        }

        public ElementType getElementType() {
            return elementType;
        }
    }

    public enum TileType implements TileModel.TileType {
        Border(1, Element.Border),
        BorderCorner(2, Element.BorderCorner);

        Integer idx;
        List<Element> elements;

        TileType(Integer idx, Element... elements) {
            this.idx = idx;
            this.elements = Arrays.asList(elements);
        }

        public List<? extends TileModel.Element> getElements() {
            return elements;
        }

        public Integer getIdx() {
            return idx;
        }
    }

    public static final Map<Integer, TileType> TILES = new HashMap<Integer, TileType>() {{
        for (TileType tileType : TileType.values()) {
            put(tileType.getIdx(), tileType);
        }
    }};

    public BorderTileModel(AssetManager am, TileType type) {
        super(am, "Models/map/borders.blend", Arrays.asList(Element.values()), type);
    }

    public BorderTileModel(AssetManager am, Integer id) {
        this(am, TILES.get(id));
    }

    public void init(Env env) {
        RigidBodyControl phy = new RigidBodyControl(0.0f);
        this.addControl(phy);
        env.getApp().getBulletAppState().getPhysicsSpace().add(phy);
    }
}
