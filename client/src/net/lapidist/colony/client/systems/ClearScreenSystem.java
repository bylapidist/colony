package net.lapidist.colony.client.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class ClearScreenSystem {

    private final Color color;

    public ClearScreenSystem(final Color colorToClear) {
        this.color = colorToClear;
    }

    public final void update() {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}

