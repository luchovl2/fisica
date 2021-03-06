package com.luciano.fisica.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.luciano.fisica.utils.Constants;
import com.luciano.fisica.utils.CreateBody;

public class Bloque extends Entidad
{
    public Bloque(World world, Vector2 pos, float width, float height, float angle)
    {
        sprite = new Sprite(new Texture(Constants.BLOQUE_TEXTURE));
        sprite.setSize(width, height);
        sprite.setOriginCenter();

        body = CreateBody.createBodyBox(world, pos.x, pos.y, width, height, 1f, false);
    }

    public void update(float delta)
    {

    }
}
