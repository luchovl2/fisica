package com.luciano.fisica.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.luciano.fisica.utils.Constants;
import com.luciano.fisica.utils.CreateBody;

public class Rampa extends Entidad
{
    private float width;
    private float height;

    public Rampa(World world, Vector2 pos, float width, float height)
    {
        this.width = width;
        this.height = height;

        sprite = new Sprite(new Texture(Constants.RAMPA_TEXTURE));
        sprite.setSize(width, height);
        sprite.setOriginCenter();

        body = CreateBody.createBodyRamp(world, pos.x, pos.y, width, height, 1f, true);
    }

    public void render(SpriteBatch batch)
    {
        if(sprite != null && body != null)
        {
            sprite.setBounds(body.getPosition().x, body.getPosition().y, width, height);
            sprite.draw(batch);
        }
    }
}
