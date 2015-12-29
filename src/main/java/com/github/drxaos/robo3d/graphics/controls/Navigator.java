package com.github.drxaos.robo3d.graphics.controls;

import com.github.drxaos.robo3d.graphics.App;
import com.github.drxaos.robo3d.graphics.Env;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public class Navigator implements RawInputListener {
    App app;
    InputManager inputManager;
    Camera cam;
    Node rootNode;

    boolean ldown = false;
    boolean rdown = false;
    boolean dragging = false;
    boolean rotating = false;
    long clickTime = 0;
    private Vector2f clickPos = new Vector2f();

    private Vector3f camLookAt = new Vector3f();
    private float camDistance = 1;
    private float camYaw = 0;
    private float camRoll = 0;

    private Vector3f toCamLookAt = new Vector3f();
    private float toCamDistance = 1;
    private float toCamYaw = 0;
    private float toCamRoll = 0;

    private float boundX = 100;
    private float boundZ = 100;

    private Vector3f camLocation = new Vector3f();
    private Quaternion camRotation = new Quaternion();
    private Vector3f camForward = new Vector3f(0, 0, 0);
    private Vector2f drag = new Vector2f();

    public Navigator(Env env) {
        this.app = env.getApp();
        this.inputManager = app.getInputManager();
        this.cam = app.getCamera();
        this.rootNode = app.getRootNode();
    }

    public void move(float x, float z, float roll, float yaw, float distance) {
        toCamLookAt.setX(x);
        toCamLookAt.setZ(z);
        toCamRoll = roll;
        toCamYaw = yaw;
        toCamDistance = distance;
    }

    public void setBounds(float boundX, float boundZ) {
        this.boundX = boundX;
        this.boundZ = boundZ;
    }

    public void lookAt(float x, float z) {
        camLookAt.setX(x);
        camLookAt.setZ(z);
    }

    public void setCamRoll(float camRoll) {
        this.camRoll = camRoll;
    }

    public void setCamYaw(float camYaw) {
        this.camYaw = camYaw;
    }

    public void setCamDistance(float camDistance) {
        this.camDistance = camDistance;
    }

    public void updateCam() {
        if (toCamYaw < FastMath.PI / 15) {
            toCamYaw = FastMath.PI / 15;
        }
        if (toCamYaw > FastMath.PI / 2 - 0.01f) {
            toCamYaw = FastMath.PI / 2 - 0.01f;
        }
        if (toCamDistance > (boundX + boundZ) * 3) {
            toCamDistance = (boundX + boundZ) * 3;
        }
        if (toCamDistance < 30) {
            toCamDistance = 30;
        }
        if (toCamLookAt.getX() < 0) {
            toCamLookAt.setX(0);
        }
        if (toCamLookAt.getZ() < 0) {
            toCamLookAt.setZ(0);
        }
        if (toCamLookAt.getX() > boundX) {
            toCamLookAt.setX(boundX);
        }
        if (toCamLookAt.getZ() > boundZ) {
            toCamLookAt.setZ(boundZ);
        }

        camDistance = (camDistance * 2 + toCamDistance) / 3;
        camRoll = (camRoll * 2 + toCamRoll) / 3;
        camYaw = (camYaw * 2 + toCamYaw) / 3;
        camLookAt.addLocal(camLookAt).addLocal(toCamLookAt).multLocal(1f / 3);

        camForward.setZ(camDistance);
        camRotation.fromAngles(-camYaw, camRoll, 0);
        camLocation.set(camLookAt).addLocal(camRotation.mult(camForward));
        cam.setLocation(camLocation);
        cam.lookAt(camLookAt, Vector3f.UNIT_Y);
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
        if (ldown) {
            dragging = true;
            inputManager.setCursorVisible(false);
            drag.set(-evt.getDX(), evt.getDY()).multLocal(camDistance / 400);
            drag.rotateAroundOrigin(camRoll, true);
            toCamLookAt.addLocal(drag.x, 0, drag.y);
        } else if (rdown) {
            rotating = true;
            inputManager.setCursorVisible(false);
            toCamRoll += -0.01f * evt.getDX();
            toCamYaw += -0.01f * evt.getDY();
        }

        if (evt.getDeltaWheel() != 0) {
            toCamDistance += -1f * evt.getDeltaWheel() / 15f;
            // TODO move look point closer to mouse
//            Vector3f groundPoint = getGroundPoint();
//            toCamLookAt.distance(groundPoint);
        }
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        if (evt.isPressed()) {
            if (evt.getButtonIndex() == MouseInput.BUTTON_LEFT && !rdown) {
                ldown = true;

                if (System.currentTimeMillis() - clickTime < 450 &&
                        clickPos.getX() - evt.getX() < 5 &&
                        clickPos.getY() - evt.getY() < 5) {
                    toCamLookAt.set(getGroundPoint());
                }
                clickTime = System.currentTimeMillis();
                clickPos.set(evt.getX(), evt.getY());
            }
            if (evt.getButtonIndex() == MouseInput.BUTTON_RIGHT && !ldown) {
                rdown = true;
            }
        }
        if (evt.isReleased()) {
            if (evt.getButtonIndex() == MouseInput.BUTTON_LEFT) {
                ldown = false;
            }
            if (evt.getButtonIndex() == MouseInput.BUTTON_RIGHT) {
                rdown = false;
            }
            dragging = false;
            rotating = false;
            inputManager.setCursorVisible(true);
        }
    }

    protected Vector3f getGroundPoint() {
        CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        rootNode.collideWith(ray, results);
        if (results.size() > 0) {
            CollisionResult collision = results.getClosestCollision();
            return collision.getContactPoint().setY(0);
        }
        return null;
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {

    }

    @Override
    public void onTouchEvent(TouchEvent evt) {

    }
}
