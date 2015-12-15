package com.github.drxaos.robo3d.graphics.models;

import com.jme3.asset.AssetManager;

public class ExampleModel extends AnimatedModel {

    public final static String ANIMATION_IDLE = "Idle";
    public final static String ANIMATION_ATTACK_BOAT = "Attack";
    public final static String ANIMATION_SUCKED_DOWN = "Blow";

    public ExampleModel(AssetManager am) {
        super(am, "Models/example/example.blend");
    }
}
