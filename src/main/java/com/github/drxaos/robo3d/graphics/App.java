package com.github.drxaos.robo3d.graphics;

import com.github.drxaos.robo3d.graphics.map.MapLoader;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;

public class App extends SimpleApplication {

    private boolean mDisplayGraphicalStats = false;
    private Node mSceneNode;
    private CameraNode mCameraNode;
    private FutureUpdater mFutureUpdater;
    private MapLoader mMapLoader;
    private Lights mLights;
    private Env env;

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

        cam.setLocation(new Vector3f(3, 2, 3));
        cam.lookAt(new Vector3f(0, 0, 0), cam.getUp());
        flyCam.setDragToRotate(true);
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(10.0f);
        cam.setFrustumFar(4000.0f);
//        cam.setFrustumNear(0.1f);

        inputManager.setCursorVisible(true);

        mSceneNode = new Node("Scene");
        rootNode.attachChild(mSceneNode);

        env = new Env(this, assetManager, viewPort, cam);

        mMapLoader.loadTo(env);
        mLights.setLights(env);
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

        cam.getLocation().setY(2);
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
