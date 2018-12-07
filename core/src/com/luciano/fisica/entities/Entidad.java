package com.luciano.fisica.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;

public class Entidad
{
    protected Sprite sprite;
    public Body body;

    public void render(SpriteBatch batch)
    {
        if(sprite != null && body != null)
        {
            sprite.setCenter(body.getPosition().x, body.getPosition().y);
            sprite.setRotation(body.getAngle() * 180f / MathUtils.PI);
            sprite.draw(batch);
        }
    }

    public void changeMass(float newMass)
    {
        if(body != null)
        {
            MassData massData = body.getMassData();
            massData.mass = newMass;
            body.setMassData(massData);
        }
    }
}
