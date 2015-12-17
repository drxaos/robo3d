package com.github.drxaos.robo3d.graphics.models;

import com.jme3.asset.AssetManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallTileModel extends TileModel {

    public enum Element implements TileModel.Element {
        Wall(ElementType.Wall),
        WallBranch(ElementType.Wall),
        WallBranchThin(ElementType.Wall),
        WallColumn(ElementType.Wall),
        WallColumnThin(ElementType.Wall),
        WallCorner(ElementType.Wall),
        WallCornerThin(ElementType.Wall),
        WallCross(ElementType.Wall),
        WallCrossThin(ElementType.Wall),
        WallHalf(ElementType.Wall),
        WallHalfR(ElementType.Wall),
        WallHalfThin(ElementType.Wall),
        WallHalfThinR(ElementType.Wall),
        WallSingle(ElementType.Wall),
        WallSingleR(ElementType.Wall),
        WallSingleThin(ElementType.Wall),
        WallSingleThinR(ElementType.Wall),
        WallThin(ElementType.Wall);

        ElementType elementType;

        Element(ElementType elementType) {
            this.elementType = elementType;
        }

        public ElementType getElementType() {
            return elementType;
        }
    }

    public enum TileType implements TileModel.TileType {
        Wall(1, Element.Wall),
        Corner(2, Element.WallCorner),
        Branch(3, Element.WallBranch),
        Cross(4, Element.WallCross),
        Single(5, Element.WallSingle),
        Hole(6, Element.WallSingle, Element.WallSingleR),
        HoleThin(7, Element.WallSingleThin, Element.WallSingleThinR),
        HoleWide(8, Element.WallHalf, Element.WallHalfR),
        HoleWideThin(9, Element.WallHalfThin, Element.WallHalfThinR),
        Column(10, Element.WallColumn),
        WallThin(11, Element.WallThin),
        CornerThin(12, Element.WallCornerThin),
        BranchThin(13, Element.WallBranchThin),
        CrossThin(14, Element.WallCrossThin),
        ColumnThin(15, Element.WallColumnThin);

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

    public WallTileModel(AssetManager am, TileType type) {
        super(am, "Models/map/walls.blend", Arrays.asList(Element.values()), type);
    }

    public WallTileModel(AssetManager am, Integer id) {
        this(am, TILES.get(id));
    }
}
