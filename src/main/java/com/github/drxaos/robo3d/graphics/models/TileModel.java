package com.github.drxaos.robo3d.graphics.models;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileModel extends StaticModel {
    enum ElementType {
        Floor(RenderQueue.ShadowMode.Receive),
        Wall(RenderQueue.ShadowMode.CastAndReceive),
        Ceil(RenderQueue.ShadowMode.CastAndReceive),
        Door(RenderQueue.ShadowMode.CastAndReceive),
        Object(RenderQueue.ShadowMode.CastAndReceive),
        Display(RenderQueue.ShadowMode.Off),
        Camera(RenderQueue.ShadowMode.Off),
        None(RenderQueue.ShadowMode.Off);

        RenderQueue.ShadowMode shadowMode;

        ElementType(RenderQueue.ShadowMode shadowMode) {
            this.shadowMode = shadowMode;
        }

        public RenderQueue.ShadowMode getShadowMode() {
            return shadowMode;
        }
    }

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
        super(am, path);

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
    }
}
