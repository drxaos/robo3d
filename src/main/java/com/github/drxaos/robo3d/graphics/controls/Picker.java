package com.github.drxaos.robo3d.graphics.controls;

import com.github.drxaos.robo3d.graphics.App;
import com.github.drxaos.robo3d.graphics.Env;
import com.github.drxaos.robo3d.graphics.filters.SelectionAppState;
import com.github.drxaos.robo3d.graphics.models.ObjectModel;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Picker implements RawInputListener {
    App app;
    InputManager inputManager;
    Camera cam;
    Node rootNode;
    boolean pressed = false;
    boolean dragging = false;
    private boolean enabled = true;

    public Picker(Env env) {
        this.app = env.getApp();
        this.inputManager = app.getInputManager();
        this.cam = app.getCamera();
        this.rootNode = app.getRootNode();
    }

    public void action(boolean click) {
        // Reset results list.
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        // Aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, dir);
        // Collect intersections between ray and all nodes in results list.
        rootNode.collideWith(ray, results);
        // Use the results -- we rotate the selected geometry.
        if (results.size() > 0) {
            for (CollisionResult result : results) {
                Geometry geometry = result.getGeometry();
                if (!"Picker".equals(geometry.getMaterial().getName())) {
                    continue;
                }
                Spatial e = geometry;
                while (e != null) {
                    if (e instanceof ObjectModel) {
                        app.select((ObjectModel) e, click ?
                                SelectionAppState.Type.SELECT :
                                SelectionAppState.Type.HOVER);
                        return;
                    }
                    e = e.getParent();
                }
            }
            app.select(null, click ?
                    SelectionAppState.Type.SELECT :
                    SelectionAppState.Type.HOVER);
        }
    }

    @Override
    public void beginInput() {

    }

    @Override
    public void endInput() {

    }

    @Override
    public void onJoyAxisEvent(JoyAxisEvent evt) {

    }

    @Override
    public void onJoyButtonEvent(JoyButtonEvent evt) {

    }

    @Override
    public void onMouseMotionEvent(MouseMotionEvent evt) {
        if (!enabled) {
            return;
        }
        if (pressed) {
            dragging = true;
        } else {
            action(false);
        }
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        if (!enabled) {
            return;
        }
        if (evt.isPressed()) {
            pressed = true;
        }
        if (evt.isReleased()) {
            if (!dragging) {
                action(true);
            }
            pressed = false;
            dragging = false;
        }
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {

    }

    @Override
    public void onTouchEvent(TouchEvent evt) {

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
