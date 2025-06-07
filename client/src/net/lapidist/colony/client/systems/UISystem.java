package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;
import net.lapidist.colony.client.core.events.payloads.ResizePayload;

public final class UISystem extends BaseSystem {

    private static final int FPS_MARGIN = 32;
    private final ScreenViewport viewport = new ScreenViewport();
    private final BitmapFont font = new BitmapFont();
    private final SpriteBatch batch = new SpriteBatch();

    public UISystem() {
        Events.getInstance().addListener(
                event -> onResize((ResizePayload) event.extraInfo),
                EventType.RESIZE.getOrdinal()
        );
    }

    private boolean onResize(final ResizePayload payload) {
        viewport.update(payload.getWidth(), payload.getHeight(), true);
        return true;
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
