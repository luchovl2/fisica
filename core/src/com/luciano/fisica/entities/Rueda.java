package com.luciano.fisica.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.luciano.fisica.utils.Constants;
import com.luciano.fisica.utils.CreateBody;

public class Rueda
{
    private Sprite sprite;
    public Body body;

    public Rueda(World world, Vector2 pos, float radius)
    {
        sprite = new Sprite(new Texture(Constants.RUEDA_TEXTURE));
        sprite.setSize(radius*2f, radius*2f);
        sprite.setOriginCenter();

        body = CreateBody.createBodyBall(world, pos.x, pos.y, radius, false);
    }

    public void update(float delta)
    {

    }

    public void render(SpriteBatch batch)
    {
        if(sprite != null && body != null)
        {
            sprite.setCenter(body.getPosition().x, body.getPosition().y);
            sprite.setRotation(body.getAngle());
            sprite.draw(batch);
        }
    }

}
