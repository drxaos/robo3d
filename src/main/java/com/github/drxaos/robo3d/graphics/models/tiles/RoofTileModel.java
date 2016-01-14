package com.github.drxaos.robo3d.graphics.models.tiles;

import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoofTileModel extends TileModel {
    public static final boolean STATIC_LAYER = true;

    public enum Element implements TileModel.Element {
        Ceil(ElementType.Ceil),
        CeilHalf(ElementType.Ceil),
        CeilHalfThin(ElementType.Ceil),
        CeilQuarter(ElementType.Ceil),
        CeilQuarterThin(ElementType.Ceil);

        ElementType elementType;

        Element(ElementType elementType) {
            this.elementType = elementType;
        }

        public ElementType getElementType() {
            return elementType;
        }
    }

    public enum TileType implements TileModel.TileType {
        Full(1, Element.Ceil),
        Half(2, Element.CeilHalf),
        Quarter(3, Element.CeilQuarter),
        HalfThin(4, Element.CeilHalfThin),
        QuarterThin(5, Element.CeilQuarterThin);

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

    public RoofTileModel(AssetManager am, TileType type) {
        super(am, "Models/map/roofs.blend", Arrays.asList(Element.values()), type);
    }

    public RoofTileModel(AssetManager am, Integer id) {
        this(am, TILES.get(id));
    }

    @Override
    protected void prepare() {
        super.prepare();
        // fix z-fighting
        List<Geometry> roofs = JmeUtils.findGeometryByMaterial(this, "RoofMat");
        for (Geometry roof : roofs) {
            roof.getMaterial().getAdditionalRenderState().setPolyOffset(-1, -1);
        }
    }
}
