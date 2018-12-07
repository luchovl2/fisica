package com.luciano.fisica.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Grid
{
    private float stepX;
    private float stepY;
    private float width;
    private float height;

    public Grid(float stepX, float stepY, float width, float height)
    {
        this.stepX = stepX;
        this.stepY = stepY;
        this.width = width;
        this.height = height;
    }

    public void render(ShapeRenderer render)
    {
        int cantLinesX = Math.round(width / stepX);
        int cantLinesY = Math.round(height / stepY);

        Color original = render.getColor();
        render.setColor(1, 1, 0, 0.2f);

        for(int i=0; i<cantLinesX; i++)
        {
            render.line(0+i*stepX, 0, 0+i*stepX, height);
        }

        for(int j=0; j<cantLinesY; j++)
        {
            render.line(0, 0+j*stepY, width, 0+j*stepY);
        }

        render.setColor(original);
    }
}
