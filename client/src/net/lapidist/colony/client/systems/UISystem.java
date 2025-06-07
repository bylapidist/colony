package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.events.ResizeEvent;
import net.mostlyoriginal.api.event.common.Subscribe;

public final class UISystem extends BaseSystem {

    private static final int FPS_MARGIN = 32;
    private final ScreenViewport viewport = new ScreenViewport();
    private final BitmapFont font = new BitmapFont();
    private final SpriteBatch batch = new SpriteBatch();

    @Subscribe
    private void onResize(final ResizeEvent event) {
        viewport.update(event.getWidth(), event.getHeight(), true);
    }

    @Override
    protected void processSystem() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
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
