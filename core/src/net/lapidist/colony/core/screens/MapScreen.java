package net.lapidist.colony.core.screens;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.GamePauseEvent;
import net.lapidist.colony.core.events.logic.GameResumeEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.io.FileLocation;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.gui.GuiAssetSystem;
import net.lapidist.colony.core.systems.map.MapAssetSystem;
import net.lapidist.colony.core.systems.render.ClearScreenSystem;
import net.lapidist.colony.core.systems.render.GuiRenderSystem;
import net.lapidist.colony.core.systems.render.MapRenderSystem;
import net.lapidist.colony.core.systems.render.RenderBatchingSystem;

public class MapScreen implements Screen {

    private World world;

    public MapScreen() {
        final RenderBatchingSystem renderBatchingSystem;

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new SuperMapper(),
                        new TagManager()
                )
                .with(WorldConfigurationBuilder.Priority.NORMAL,
                        new ClearScreenSystem(Color.GOLD),
                        new CameraSystem(1),
                        new MapAssetSystem(FileLocation.INTERNAL),
                        new GuiAssetSystem(FileLocation.INTERNAL),
                        renderBatchingSystem = new RenderBatchingSystem(),
                        new MapRenderSystem(renderBatchingSystem),
                        new GuiRenderSystem(renderBatchingSystem)
                )
                .build();

        world = new World(config);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(final float delta) {
        world.setDelta(delta);
        world.process();
    }

    @Override
    public void resize(final int width, final int height) {
        Events.fire(new ScreenResizeEvent(width, height));
    }

    @Override
    public void pause() {
        Events.fire(new GamePauseEvent());
    }

    @Override
    public void resume() {
        Events.fire(new GameResumeEvent());
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
