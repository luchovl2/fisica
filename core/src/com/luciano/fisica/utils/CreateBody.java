package com.luciano.fisica.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;

import static com.luciano.fisica.utils.Constants.PPM;

public class CreateBody
{
    public static Body createBody(World world, float posX, float posY, Shape shape, float roza, boolean isStatic)
    {
        Body body;
        BodyDef def = new BodyDef();
        FixtureDef fixdef = new FixtureDef();

        def.type = BodyDef.BodyType.DynamicBody;
        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;

        def.position.set(posX, posY);
        //def.fixedRotation = true;

        body = world.createBody(def);

        fixdef.shape = shape;
        fixdef.density = 1f;
        fixdef.friction = roza;

        body.createFixture(fixdef);
        return body;
    }

    public static Body createCompoundBody(World world, float posX, float posY, Shape shape, float roza, boolean isStatic)
    {
        Body body;
        BodyDef def = new BodyDef();
        FixtureDef fixdef = new FixtureDef();

        def.type = BodyDef.BodyType.DynamicBody;
        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;

        def.position.set(posX, posY);
        //def.fixedRotation = true;

        body = world.createBody(def);

        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(50/2/PPM, 40/2/PPM);

        fixdef.shape = shape2;
        fixdef.density = 1f;
        fixdef.friction = roza;

        body.createFixture(fixdef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(10/PPM);
        circleShape.setPosition(new Vector2(0f, 30/PPM));

        fixdef.shape = circleShape;
        body.createFixture(fixdef);

        circleShape.setPosition(new Vector2(0f, -30/PPM));
        fixdef.shape = circleShape;
        body.createFixture(fixdef);

        circleShape.dispose();
        shape2.dispose();
        return body;
    }

    public static Body createBodyBox(World world, float posX, float posY, float width, float height, float roza, boolean isStatic)
    {
        Body body;
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(width/2, height/2);
        body = createBody(world, posX, posY, shape, roza, isStatic);
        shape.dispose();
        return body;
    }

    public static Body createBodyBall(World world, float posX, float posY, float radius, boolean isStatic)
    {
        Body body;
        CircleShape shape = new CircleShape();

        shape.setRadius(radius);
        body = createBody(world, posX, posY, shape, 1, isStatic);
        shape.dispose();
        return body;
    }

    public static Body createBodyRamp(World world, float posX, float posY, float width, float height, float roza, boolean isStatic)
    {
        Body body;
        PolygonShape shape = new PolygonShape();

        Vector2[] vertices = new Vector2[] {
                new Vector2(0, 0),
                new Vector2(width, 0),
                new Vector2(width, height)
        };
        shape.set(vertices);
        body = createBody(world, posX, posY, shape, roza, isStatic);
        shape.dispose();
        return body;
    }

    public static void addBodyPart(Body body, Shape shape)
    {
        FixtureDef fixdef = new FixtureDef();

        fixdef.shape = shape;
        fixdef.friction = body.getFixtureList().get(0).getFriction();
        fixdef.restitution = body.getFixtureList().get(0).getRestitution();

        body.createFixture(fixdef);
    }

    public static void agregarPolea(World world, Body bodyA, Body bodyB, Body groundA, Body groundB)
    {
        PulleyJointDef jointDef = new PulleyJointDef();
        jointDef.initialize(bodyA, bodyB,
                groundA.getWorldCenter(),
                groundB.getWorldCenter(),
                bodyA.getWorldCenter(),
                bodyB.getWorldCenter(),
                1);
        jointDef.lengthA = 0.5f + groundA.getPosition().dst(jointDef.groundAnchorB);
        jointDef.lengthB = 0.4f;
        world.createJoint(jointDef);
    }
}
