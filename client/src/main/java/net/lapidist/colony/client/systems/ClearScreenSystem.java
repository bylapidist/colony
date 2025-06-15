package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public final class ClearScreenSystem extends BaseSystem {

    private final Color color;

    public ClearScreenSystem(final Color colorToClear) {
        this.color = colorToClear;
    }

    /** Access the mutable clear color used each frame. */
    public Color getColor() {
        return color;
    }

    @Override
    protected void processSystem() {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_ALPHA_BITS);
    }
}
