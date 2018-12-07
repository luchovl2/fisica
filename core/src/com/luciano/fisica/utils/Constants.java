package com.luciano.fisica.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public final class Constants
{
    public static final float PPM = 100;

    public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;

    public static final int UI_ANCHO = 300;
    public static final int UI_ALTO = 200;
    public static final int UI_POSX = 1050;

    public static final String BLOQUE_TEXTURE = "brick.png";
    public static final String RUEDA_TEXTURE = "rueda.png";
    public static final String PARED_TEXTURE = "pared.png";
    public static final String RAMPA_TEXTURE = "brick_triang.png";

    public static final Vector2 GRAVEDAD_2D = new Vector2(0f, -9.81f);

    public static final float PARED_GROSOR = 20/PPM;
    public static final float PARED_ROZAMIENTO = 0.4f;
}
