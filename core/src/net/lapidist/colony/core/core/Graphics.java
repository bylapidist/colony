package net.lapidist.colony.core.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Graphics {

    public static int width() {
        return Gdx.graphics.getWidth();
    }

    public static int height() {
        return Gdx.graphics.getHeight();
    }

    public static void clear(Color color) {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_ALPHA_BITS);
    }

    public static void draw(Sprite sprite) {
        sprite.draw(Core.spriteBatch);
    }

    public static void begin() {
        Core.spriteBatch.setProjectionMatrix(Core.camera.combined);
        Core.spriteBatch.begin();
    }

    public static void end() {
        Core.spriteBatch.end();
    }

    public static void resize() {
        if (Gdx.graphics.getWidth() <= 2 || Gdx.graphics.getHeight() <= 2) return;
    }
}
