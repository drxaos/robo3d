package com.github.drxaos.robo3d.graphics.models;

import com.jme3.asset.AssetManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroundTileModel extends TileModel {

    public enum Element implements TileModel.Element {
        Ground(ElementType.Floor),
        Ceil(ElementType.Ceil),
        CeilHalf(ElementType.Ceil),
        CeilHalfR(ElementType.Ceil),
        CeilQuarter(ElementType.Ceil),
        Camera(ElementType.None),
        DisplayCamera(ElementType.Camera),
        DisplayHalfCamera(ElementType.Camera),
        Display(ElementType.Display),
        DisplayBorder(ElementType.Display),
        DisplayHalf(ElementType.Display),
        DisplayBorderHalf(ElementType.Display),
        Door(ElementType.Door),
        DoorWide(ElementType.Door),
        Wall(ElementType.Wall),
        WallBranch(ElementType.Wall),
        WallColumn(ElementType.Wall),
        WallCorner(ElementType.Wall),
        WallCross(ElementType.Wall),
        WallHalf(ElementType.Wall),
        WallHalfR(ElementType.Wall),
        WallSingle(ElementType.Wall),
        WallSingleR(ElementType.Wall);

        ElementType elementType;

        Element(ElementType elementType) {
            this.elementType = elementType;
        }

        public ElementType getElementType() {
            return elementType;
        }
    }

    public enum TileType implements TileModel.TileType {
        Empty(1, Element.Ground),
        Wall(2, Element.Ground, Element.Wall),
        Corner(3, Element.Ground, Element.WallCorner),
        Branch(4, Element.Ground, Element.WallBranch),
        Cross(5, Element.Ground, Element.WallCross),
        Hole(6, Element.Ground, Element.WallSingle, Element.WallSingleR),
        Door(7, Element.Ground, Element.WallSingle, Element.WallSingleR, Element.Door, Element.Display, Element.DisplayBorder, Element.Camera),
        HoleWide(8, Element.Ground, Element.WallHalf, Element.WallHalfR),
        DoorWide(9, Element.Ground, Element.WallHalf, Element.WallHalfR, Element.DoorWide, Element.DisplayHalf, Element.DisplayBorderHalf, Element.DisplayHalfCamera),
        Column(10, Element.Ground, Element.WallColumn),
        Single(11, Element.Ground, Element.WallSingle);

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

    public GroundTileModel(AssetManager am, TileType type) {
        super(am, "Models/map/map.blend", Arrays.asList(Element.values()), type);
    }
}
