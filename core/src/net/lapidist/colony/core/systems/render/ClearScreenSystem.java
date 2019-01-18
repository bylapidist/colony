package net.lapidist.colony.core.systems.render;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class ClearScreenSystem extends BaseSystem {

    private final Color color;

    public ClearScreenSystem() {
        this(Color.BLACK);
    }

    public ClearScreenSystem(Color color) {
        this.color = color;
    }

    @Override
    protected void processSystem() {
        Gdx.gl20.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_ALPHA_BITS);
    }
}
