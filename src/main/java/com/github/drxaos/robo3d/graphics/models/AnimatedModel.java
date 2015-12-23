package com.github.drxaos.robo3d.graphics.models;

import com.jme3.animation.*;
import com.jme3.asset.AssetManager;

public class AnimatedModel extends StaticModel {

    private final static float BLEND_TIME = 0.2f;
    private AnimControl mControl;
    private AnimChannel mChannel;

    public AnimatedModel(AssetManager am, String meshName) {
        super(am, meshName, null);

        mControl = this.getControl(AnimControl.class);
        mChannel = mControl.createChannel();

        SkeletonControl skeletonControl = this.getControl(SkeletonControl.class);
        skeletonControl.setHardwareSkinningPreferred(true);
    }

    public void playAnimation(String animation) {
        playAnimation(animation, true, true, null);
    }

    public void playAnimation(String animation, boolean loop, boolean animateTransition,
                              AnimEventListener listener) {
        mChannel.setLoopMode(loop ? LoopMode.Loop : LoopMode.DontLoop);
        mChannel.setAnim(animation, animateTransition ? BLEND_TIME : 0.0f);

        if (listener != null) {
            mChannel.getControl().addListener(listener);
        }

        if (loop) {
            mChannel.setTime((float) Math.random() * mChannel.getAnimMaxTime());
        }
    }

    public void printAnimations() {
        for (String anim : mControl.getAnimationNames()) {
            System.out.println(anim);
        }
    }
}
