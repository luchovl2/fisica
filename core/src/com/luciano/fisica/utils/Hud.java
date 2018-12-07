package com.luciano.fisica.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud
{
    public Stage stage;
    private Viewport viewport;

    private float hudTime;
    private Label timeLabel;

    public Hud(SpriteBatch batch, float width, float height)
    {
        hudTime = 0f;
        viewport = new FitViewport(width, height, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label(String.format("%3.3f s", hudTime), new Label.LabelStyle(new BitmapFont(), Color.RED));

        table.add(timeLabel).center().padTop(10);

        stage.addActor(table);
    }

    public void update(float delta)
    {
        hudTime += delta;
        timeLabel.setText(String.format("%.3f s", hudTime));
    }
}
