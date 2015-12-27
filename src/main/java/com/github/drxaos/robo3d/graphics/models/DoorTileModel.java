package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.JmeUtils;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoorTileModel extends TileModel {

    public enum Element implements TileModel.Element {
        Display(ElementType.Display),
        DisplayBorder(ElementType.Display),
        DisplayCamera(ElementType.Camera),
        Door(ElementType.Door),

        DisplayThin(ElementType.Display),
        DisplayThinBorder(ElementType.Display),
        DisplayThinCamera(ElementType.Camera),
        DoorThin(ElementType.Door),

        DisplayHalf(ElementType.Display),
        DisplayBorderHalf(ElementType.Display),
        DisplayHalfCamera(ElementType.Camera),
        DoorWide(ElementType.Door),

        DisplayHalfThin(ElementType.Display),
        DisplayBorderHalfThin(ElementType.Display),
        DisplayHalfThinCamera(ElementType.Camera),
        DoorWideThin(ElementType.Door);

        ElementType elementType;

        Element(ElementType elementType) {
            this.elementType = elementType;
        }

        public ElementType getElementType() {
            return elementType;
        }
    }

    public enum TileType implements TileModel.TileType {
        Door(1, Element.Display, Element.DisplayBorder, Element.DisplayCamera, Element.Door),
        DoorWide(2, Element.DisplayHalf, Element.DisplayBorderHalf, Element.DisplayHalfCamera, Element.DoorWide),
        DoorThin(3, Element.DisplayThin, Element.DisplayThinBorder, Element.DisplayThinCamera, Element.DoorThin),
        DoorWideThin(4, Element.DisplayHalfThin, Element.DisplayBorderHalfThin, Element.DisplayHalfThinCamera, Element.DoorWideThin);

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

    public DoorTileModel(AssetManager am, TileType type) {
        super(am, "Models/map/doors.blend", Arrays.asList(Element.values()), type);
    }

    public DoorTileModel(AssetManager am, Integer id) {
        this(am, TILES.get(id));
    }

    public void init(Env env) {
        RigidBodyControl phy = new RigidBodyControl(0.0f);
        List<Geometry> doors = JmeUtils.findGeometryByMaterial(this, "DoorMat");
        doors.get(0).getParent().addControl(phy);
        env.getApp().getBulletAppState().getPhysicsSpace().add(phy);
    }
}
