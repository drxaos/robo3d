package com.github.drxaos.robo3d.graphics.models;

import com.jme3.asset.AssetManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadTileModel extends TileModel {
    public static final boolean STATIC_LAYER = true;

    public enum Element implements TileModel.Element {
        Ground(ElementType.Floor),
        GroundHalf(ElementType.Floor),
        GroundQuarter(ElementType.Floor),
        RoadCorner(ElementType.Floor),
        Road(ElementType.Floor),
        Floor(ElementType.Floor),
        FloorHalf(ElementType.Floor),
        FloorQuarter(ElementType.Floor);

        ElementType elementType;

        Element(ElementType elementType) {
            this.elementType = elementType;
        }

        public ElementType getElementType() {
            return elementType;
        }
    }

    public enum TileType implements TileModel.TileType {
        Ground(1, Element.Ground),
        RoadCorner(2, Element.RoadCorner),
        Road(3, Element.Road),
        Floor(4, Element.Floor),
        FloorHalf(5, Element.GroundHalf, Element.FloorHalf),
        FloorQuarter(6, Element.GroundQuarter, Element.FloorQuarter);

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

    public RoadTileModel(AssetManager am, TileType type) {
        super(am, "Models/map/roads.blend", Arrays.asList(Element.values()), type);
    }

    public RoadTileModel(AssetManager am, Integer id) {
        this(am, TILES.get(id));
    }

}
