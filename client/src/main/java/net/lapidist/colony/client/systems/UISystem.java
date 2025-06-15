package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.client.events.ResizeEvent;
import net.lapidist.colony.util.I18n;
import net.mostlyoriginal.api.event.common.Subscribe;

public final class UISystem extends BaseSystem {

    private static final int FPS_MARGIN = 32;
    private final ScreenViewport viewport = new ScreenViewport();
    private final Stage stage;
    private final BitmapFont font = new BitmapFont();
    private final SpriteBatch batch = new SpriteBatch();

    public UISystem(final Stage stageToSet) {
        this.stage = stageToSet;
    }

    @Subscribe
    private void onResize(final ResizeEvent event) {
        viewport.update(event.width(), event.height(), true);
        stage.getViewport().update(event.width(), event.height(), true);
    }

    @Override
    protected void processSystem() {
        stage.act(world.getDelta());
        stage.draw();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.draw(
                batch,
                Gdx.graphics.getFramesPerSecond() + I18n.get("ui.fpsSuffix"),
                FPS_MARGIN,
                FPS_MARGIN
        );
        batch.end();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        font.dispose();
    }
}
