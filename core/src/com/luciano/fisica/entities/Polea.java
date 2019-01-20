package com.luciano.fisica.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.luciano.fisica.utils.CreateBody;

public class Polea extends Entidad
{
    public Polea(World world, Vector2 pos, Entidad entA, Entidad entB, float radius)
    {
        Body bodyA = entA.body;
        Body bodyB = entB.body;

        body = CreateBody.createBodyBall(world, pos.x, pos.y, radius, true);

        Vector2 groundA = new Vector2(body.getPosition().x - radius, body.getPosition().y);
        Vector2 groundB = body.getWorldCenter().add(radius, 0f);

        Gdx.app.log("POLEA", "A: " + groundA + " B: " + groundB);

        PulleyJointDef jointDef = new PulleyJointDef();
        jointDef.initialize(bodyA, bodyB,
                groundA,
                groundB,
                bodyA.getWorldCenter(),
                bodyB.getWorldCenter(),
                1f);

        world.createJoint(jointDef);
    }
}
