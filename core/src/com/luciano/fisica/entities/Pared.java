package com.luciano.fisica.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.luciano.fisica.utils.Constants;
import com.luciano.fisica.utils.CreateBody;

public class Pared
{
    private Sprite sprite;
    private Body body;
    private World world;

    public Pared(World world, Vector2 pos, float width, float height)
    {
        this.world = world;

        sprite = new Sprite(new Texture(Constants.PARED_TEXTURE));
        sprite.setSize(width, height);
        sprite.setOriginCenter();

        crearPared(pos, width, height);
    }

    private void crearPared(Vector2 pos, float width, float height)
    {
        body = CreateBody.createBodyBox(world,
                pos.x,
                pos.y,
                width,
                height,
                Constants.PARED_ROZAMIENTO,
                true);
    }

    public void render(SpriteBatch batch)
    {
        if(sprite != null && body != null)
        {
            sprite.setCenter(body.getPosition().x, body.getPosition().y);
            sprite.draw(batch);
        }
    }
}
