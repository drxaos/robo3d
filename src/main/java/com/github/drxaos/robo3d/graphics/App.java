package com.github.drxaos.robo3d.graphics;

import com.github.drxaos.robo3d.graphics.controls.Navigator;
import com.github.drxaos.robo3d.graphics.controls.Picker;
import com.github.drxaos.robo3d.graphics.filters.SelectionAppState;
import com.github.drxaos.robo3d.graphics.gui.Info;
import com.github.drxaos.robo3d.graphics.gui.InfoAppState;
import com.github.drxaos.robo3d.graphics.map.MapLoader;
import com.github.drxaos.robo3d.graphics.models.StaticModel;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.asset.AssetEventListener;
import com.jme3.asset.AssetKey;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class App extends SimpleApplication {

    private Node mSceneNode;
    private CameraNode mCameraNode;
    private FutureUpdater mFutureUpdater;
    private MapLoader mMapLoader;
    private Lights mLights;
    private Env env;
    private BulletAppState bulletAppState;
    private SelectionAppState selectionAppState;
    private Node selectionNode;
    private Picker picker;
    private Navigator navigator;
    private StatsAppState statsAppState;
    private boolean initialized = false;

    private List<StaticModel> objects = new ArrayList<>();
    private Info info;


    public App() {
        super(null);
        mMapLoader = new MapLoader();
        mLights = new Lights();
        mFutureUpdater = new FutureUpdater();
    }

    @Override
    public void simpleInitApp() {
        inputManager.clearMappings();
        setPauseOnLostFocus(false);

        bulletAppState = new BulletAppState();
        bulletAppState.setSpeed(2);
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(false);
        bulletAppState.getPhysicsSpace().setAccuracy(1f / 100f);
        bulletAppState.getPhysicsSpace().setMaxSubSteps(5);

        stateManager.attach(new InfoAppState(guiNode, guiFont, info = new Info()));

        AssetEventListener asl = new AssetEventListener() {
            public void assetLoaded(AssetKey key) {
            }

            public void assetRequested(AssetKey key) {
                if (key.getExtension().equals("png") || key.getExtension().equals("jpg") || key.getExtension().equals("dds")) {
                    TextureKey tkey = (TextureKey) key;
                    tkey.setAnisotropy(8);
                    tkey.setGenerateMips(true);
                }
            }

            public void assetDependencyNotFound(AssetKey parentKey, AssetKey dependentAssetKey) {
            }
        };
        assetManager.addAssetEventListener(asl);

        cam.setLocation(new Vector3f(15, 30, 100));
        cam.lookAt(new Vector3f(15, 0, 100), cam.getUp());
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
//        flyCam.setDragToRotate(true);
//        flyCam.setEnabled(false);
//        flyCam.setMoveSpeed(20.0f);
        inputManager.setCursorVisible(true);
        cam.setFrustumFar(4000.0f);
//        cam.setFrustumNear(0.1f);

        selectionNode = new Node("Selection");
        rootNode.attachChild(selectionNode);
        selectionAppState = new SelectionAppState(selectionNode);
        stateManager.attach(selectionAppState);

        inputManager.setCursorVisible(true);

        mSceneNode = new Node("Scene");
        rootNode.attachChild(mSceneNode);

        env = new Env(this, assetManager, viewPort, cam);

        mLights.setLights(env);

        inputManager.addRawInputListener(picker = new Picker(env));
        inputManager.addRawInputListener(navigator = new Navigator(env));

        mMapLoader.loadTo(env);

        initialized = true;
    }

    public CameraNode getCameraNode() {
        return mCameraNode;
    }

    public FutureUpdater getFuture() {
        return mFutureUpdater;
    }

    public Node getSceneNode() {
        return mSceneNode;
    }

    @Override
    public void simpleUpdate(float tpf) {
        mFutureUpdater.update(tpf);

        for (StaticModel model : objects) {
            model.update(env);
        }

        if (cameraView) {
            selectedObject.applyFirstPersonView(cam);
        } else {
            navigator.updateCam();
        }

        updateSelectedInfo();
    }

    protected void updateSelectedInfo() {
        if (selectedObject == null) {
            info.setSelection("none");
            info.setSelectionX("---");
            info.setSelectionY("---");
            info.setSelectionRot("---");
            info.setSelectionState("---");
        } else {
            info.setSelection(selectedObject.getName());
            info.setSelectionX("" + String.format("%.2f", selectedObject.getWorldTranslation().getX()));
            info.setSelectionY("" + String.format("%.2f", selectedObject.getWorldTranslation().getZ()));
            float rot = selectedObject.getWorldRotation().inverse().mult(new Quaternion().fromAngles(0, FastMath.PI, 0)).toAngles(new float[3])[1];
            rot = (float) (rot / Math.PI * 180);
            info.setSelectionRot("" + String.format("%.2f", rot));
            Object state = selectedObject.getUserData("object_state");
            info.setSelectionState("" + (state == null ? "---" : state));
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public List<StaticModel> getObjects() {
        return objects;
    }

    public StaticModel getObject(String name) {
        for (StaticModel object : objects) {
            if (name.equals(object.getName())) {
                return object;
            }
        }
        return null;
    }

    StaticModel selectedObject;

    public void select(StaticModel object, SelectionAppState.Type type) {
        selectionAppState.highlight(object, type);
        if (type == SelectionAppState.Type.SELECT) {
            if (selectedObject != null) {
                selectedObject.selected(false);
            }
            selectedObject = object;
            if (selectedObject != null) {
                selectedObject.selected(true);
            }
        }
    }

    public void look(float x, float y, float roll, float yaw, float distance) {
        navigator.move(x, y, roll, yaw, distance);
    }

    public void size(float x, float y) {
        navigator.setBounds(x, y);
    }


    boolean cameraView = false;

    public boolean showCameraView(boolean show) {
        if (!initialized) {
            return false;
        }
        if (show && selectedObject == null) {
            return false;
        }
        cameraView = show;
        picker.setEnabled(!show);
        navigator.setEnabled(!show);
        selectionAppState.highlight(show ? null : selectedObject, SelectionAppState.Type.SELECT);

        return true;
    }

    public Info getInfo() {
        return info;
    }
}
