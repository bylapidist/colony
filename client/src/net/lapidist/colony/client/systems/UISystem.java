package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;
import net.lapidist.colony.client.core.events.payloads.ResizePayload;

public class UISystem  extends EntitySystem {

    private static final int FPS_MARGIN = 32;
    private final ScreenViewport viewport = new ScreenViewport();
    private final BitmapFont font = new BitmapFont();
    private final SpriteBatch batch = new SpriteBatch();

    public UISystem() {
        super(0);
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
    public final void addedToEngine(final Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public final void removedFromEngine(final Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public final void update(final float deltaTime) {
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
