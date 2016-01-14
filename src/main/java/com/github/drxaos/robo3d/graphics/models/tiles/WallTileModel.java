package com.github.drxaos.robo3d.graphics.models.tiles;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.models.StaticModel;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallTileModel extends TileModel {
    public static final boolean STATIC_LAYER = true;

    public enum Element implements TileModel.Element {
        Border(StaticModel.ElementType.Wall),
        BorderCorner(StaticModel.ElementType.Wall),
        Wall(StaticModel.ElementType.Wall),
        WallBranch(StaticModel.ElementType.Wall),
        WallBranchThin(StaticModel.ElementType.Wall),
        WallColumn(StaticModel.ElementType.Wall),
        WallColumnThin(StaticModel.ElementType.Wall),
        WallTwo(StaticModel.ElementType.Wall),
        WallTwoThin(StaticModel.ElementType.Wall),
        WallCorner(StaticModel.ElementType.Wall),
        WallCornerThin(StaticModel.ElementType.Wall),
        WallCross(StaticModel.ElementType.Wall),
        WallCrossThin(StaticModel.ElementType.Wall),
        WallHalf(StaticModel.ElementType.Wall),
        WallHalfR(StaticModel.ElementType.Wall),
        WallHalfThin(StaticModel.ElementType.Wall),
        WallHalfThinR(StaticModel.ElementType.Wall),
        WallSingle(StaticModel.ElementType.Wall),
        WallSingleR(StaticModel.ElementType.Wall),
        WallSingleThin(StaticModel.ElementType.Wall),
        WallSingleThinR(StaticModel.ElementType.Wall),
        WallThin(StaticModel.ElementType.Wall);

        StaticModel.ElementType elementType;

        Element(StaticModel.ElementType elementType) {
            this.elementType = elementType;
        }

        public StaticModel.ElementType getElementType() {
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
        ColumnThin(15, Element.WallColumnThin),
        Two(16, Element.WallTwo),
        TwoThin(17, Element.WallTwoThin),
        Border(18, Element.Border),
        BorderCorner(19, Element.BorderCorner);

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

    public void init(Env env) {
        RigidBodyControl phy = new RigidBodyControl(0.0f);
        this.addControl(phy);
        env.getApp().getBulletAppState().getPhysicsSpace().add(phy);
    }
}
