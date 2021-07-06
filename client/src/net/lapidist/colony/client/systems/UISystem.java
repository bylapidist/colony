package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UISystem  extends EntitySystem {

    private static final int FPS_MARGIN = 32;
    private final BitmapFont font = new BitmapFont();
    private final SpriteBatch batch = new SpriteBatch();

    public UISystem() {
        super(0);
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
        batch.begin();
        font.draw(
                batch,
                Gdx.graphics.getFramesPerSecond() + " FPS",
                FPS_MARGIN,
                Gdx.graphics.getHeight() - FPS_MARGIN
        );
        batch.end();
    }
}
