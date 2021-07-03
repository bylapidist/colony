package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class ClearScreenSystem extends EntitySystem {

    private final Color color;

    public ClearScreenSystem(final Color colorToClear) {
        this(colorToClear, 0);
    }

    public ClearScreenSystem(final Color colorToClear, final int priority) {
        super(priority);
        this.color = colorToClear;
    }

    @Override
    public final void addedToEngine(final Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public final void removedFromEngine(final Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public final void update(final float deltaTime) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}

