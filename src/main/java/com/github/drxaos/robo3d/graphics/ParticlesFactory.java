package com.github.drxaos.robo3d.graphics;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Node;

public class ParticlesFactory {

    public static Node makeWaterSplash(AssetManager assetManager) {
        Node loadedNode = (Node) assetManager.loadModel("Scenes/fx_WaterSplash.j3o");

        Material debris_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        debris_mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        ParticleEmitter emitter = (ParticleEmitter) loadedNode.getChild(0);
        emitter.setMaterial(debris_mat);
        emitter.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        emitter.getMaterial().getAdditionalRenderState().setDepthWrite(true);
        emitter.getMaterial().getAdditionalRenderState().setAlphaTest(true);
        emitter.getMaterial().getAdditionalRenderState().setAlphaFallOff(0.1f);
        emitter.setLocalScale(10);

        emitter.setParticlesPerSec(0.0f);

        return loadedNode;
    }

    public static void emitAllParticles(Node effect) {
        ParticleEmitter emitter = (ParticleEmitter) effect.getChild(0);
        emitter.emitAllParticles();
    }
}
