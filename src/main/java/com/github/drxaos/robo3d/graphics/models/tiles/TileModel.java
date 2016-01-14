package com.github.drxaos.robo3d.graphics.models.tiles;

import com.github.drxaos.robo3d.graphics.models.StaticModel;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class TileModel extends StaticModel {

    public interface Element {
        ElementType getElementType();

        String name();
    }

    public interface TileType {
        List<? extends Element> getElements();

        Integer getIdx();

        String name();
    }

    protected Map<ElementType, Node> nodes = new HashMap<ElementType, Node>();

    public TileModel(AssetManager am, String path, List<? extends Element> allElements, TileType type) {
        super(am, path, type.getClass().getName() + "." + type.name(), null);

        if (fresh) {
            // group elements
            for (Element element : allElements) {
                Spatial child = this.getChild(element.name());
                if (child == null) {
                    System.out.println("NO EL " + element.name());
                    continue;
                }
                child.removeFromParent();

                if (type.getElements().contains(element)) {
                    Node node = nodes.get(element.getElementType());
                    if (node == null) {
                        node = new Node("Group_" + element.getElementType().name());
                        nodes.put(element.getElementType(), node);
                    }
                    node.attachChild(child);
                    fixLighting(child, element.getElementType());
                }
            }

            // remove all
            for (Spatial child : this.getChildren()) {
                child.removeFromParent();
            }

            // attach groups
            for (Node node : nodes.values()) {
                this.attachChild(node);
            }

            prepare();

            applyModel();
        }
    }

    protected void prepare() {
    }

    public static final Map<String, Class<? extends TileModel>> TYPES = new HashMap<String, Class<? extends TileModel>>() {{
        put("roofs", RoofTileModel.class);
        put("walls", WallTileModel.class);
        put("doors", DoorTileModel.class);
        put("roads", RoadTileModel.class);
        put("borders", BorderTileModel.class);
    }};


}
