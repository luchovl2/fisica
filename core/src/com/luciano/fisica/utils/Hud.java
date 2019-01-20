package com.luciano.fisica.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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

    private Vector2 pos;
    private Vector2 vel;
    private Vector2 accel;
    private Label positionLabel;
    private Label velocityLabel;
    private Label accelerationLabel;

    public Hud(SpriteBatch batch, float width, float height)
    {
        hudTime = 0f;
        pos = new Vector2(0f, 0f);
        vel = new Vector2(0f, 0f);
        accel = new Vector2(0f, 0f);

        viewport = new FitViewport(width, height, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label(String.format("%3.3f s", hudTime), new Label.LabelStyle(new BitmapFont(), Color.RED));
        positionLabel = new Label(String.format("x: %.2f; y: %.2f", pos.x, pos.y),
                new Label.LabelStyle(new BitmapFont(), Color.RED));
        velocityLabel = new Label(String.format("vx: %.2f; vy: %.2f", vel.x, vel.y),
                new Label.LabelStyle(new BitmapFont(), Color.RED));
        accelerationLabel = new Label(String.format("ax: %.2f; ay: %.2f", accel.x, accel.y),
                new Label.LabelStyle(new BitmapFont(), Color.RED));

        table.add(timeLabel).center().padTop(10);
        table.row();
        table.add(positionLabel).center();
        table.row();
        table.add(velocityLabel).center();
        table.row();
        table.add(accelerationLabel).center();

        stage.addActor(table);
    }

    public void update(float delta)
    {
        hudTime += delta;
        timeLabel.setText(String.format("%.3f s", hudTime));
    }

    public void setEntityState(Vector2 pos, Vector2 vel, float delta)
    {
        this.pos = pos.cpy();
        Vector2 vel_old = this.vel.cpy();
        this.vel = vel.cpy();

        this.accel = vel_old.sub(this.vel).scl(1/delta);

        positionLabel.setText(String.format("x: %.3f; y: %.3f", pos.x, pos.y));
        velocityLabel.setText(String.format("vx: %.3f; vy: %.3f", vel.x, vel.y));

        accelerationLabel.setText(String.format("ax: %.3f; ay: %.3f", accel.x, accel.y));
    }
}
