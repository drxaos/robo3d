package com.github.drxaos.robo3d.graphics.models;

import com.github.drxaos.robo3d.graphics.map.Optimizer;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;

public class RobotModel extends ObjectModel {

    public RobotModel(AssetManager am) {
        super(am, "Models/robot/robot.blend", null);
    }

    public RobotModel(AssetManager am, String subname) {
        super(am, "Models/robot/robot.blend", subname);
    }

    @Override
    protected void prepare() {
        super.prepare();
        Optimizer.optimize(this, true);
    }

    @Override
    protected Body createBody(World world) {
        // Dynamic Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.allowSleep = false;
        bodyDef.linearDamping = 10f;
        bodyDef.angularDamping = 30f;
        bodyDef.position.set(
                this.localTransform.getTranslation().getX(),
                this.localTransform.getTranslation().getY());
        bodyDef.angle = this.localTransform.getRotation().toAngleAxis(Vector3f.UNIT_Y);
        Body body = world.createBody(bodyDef);
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(1, 1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1;
        fixtureDef.friction = 0.8f;
        body.createFixture(fixtureDef);
        return body;
    }
}
