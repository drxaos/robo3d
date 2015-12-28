package com.github.drxaos.robo3d.graphics;

import com.github.drxaos.robo3d.graphics.filters.SelectionAppState;
import com.github.drxaos.robo3d.graphics.map.MapLoader;
import com.github.drxaos.robo3d.graphics.models.StaticModel;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetEventListener;
import com.jme3.asset.AssetKey;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class App extends SimpleApplication {

    private boolean mDisplayGraphicalStats = false;
    private Node mSceneNode;
    private CameraNode mCameraNode;
    private FutureUpdater mFutureUpdater;
    private MapLoader mMapLoader;
    private Lights mLights;
    private Env env;
    private BulletAppState bulletAppState;
    private SelectionAppState selectionAppState;
    private Node selectionNode;

    private List<StaticModel> objects = new ArrayList<>();
    float timeStep = 1.0f / 60.0f;
    int velocityIterations = 6;
    int positionIterations = 2;

    public App() {
        super();
        mMapLoader = new MapLoader();
        mLights = new Lights();
        mFutureUpdater = new FutureUpdater();
    }

    @Override
    public void simpleInitApp() {
        inputManager.clearMappings();

        setDisplayFps(false);
        setDisplayStatView(false);
        setPauseOnLostFocus(true);
        toggleGraphicsStats();

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);
        bulletAppState.getPhysicsSpace().setAccuracy(1f / 100f);
        bulletAppState.getPhysicsSpace().setMaxSubSteps(10);

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
        flyCam.setDragToRotate(true);
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(20.0f);
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

        mMapLoader.loadTo(env);
        mLights.setLights(env);

        inputManager.addRawInputListener(new Picker(env));
    }

    public void toggleGraphicsStats() {
        if (!mDisplayGraphicalStats) {
            mDisplayGraphicalStats = true;
            setDisplayFps(true);
            setDisplayStatView(true);
        } else {
            mDisplayGraphicalStats = false;
            setDisplayFps(false);
            setDisplayStatView(false);
        }
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

        //cam.getLocation().setY(1.5f);

        for (StaticModel model : objects) {
            model.update(env);
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

    public void select(StaticModel object, SelectionAppState.Type type) {
        selectionAppState.highlight(object, type);
    }
}
