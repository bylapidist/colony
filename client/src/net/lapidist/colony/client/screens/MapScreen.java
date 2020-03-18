package net.lapidist.colony.client.screens;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.client.systems.render.GuiRenderSystem;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.network.ClientSystem;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.systems.player.PlayerCameraSystem;
import net.lapidist.colony.core.systems.player.PlayerControlSystem;
//import net.lapidist.colony.client.systems.render.GuiRenderSystem;
import net.lapidist.colony.core.utils.io.FileLocation;
import net.lapidist.colony.core.systems.generators.TerrainGeneratorSystem;
import net.lapidist.colony.core.systems.physics.TimeSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;
import net.lapidist.colony.core.systems.factories.LightFactorySystem;
import net.lapidist.colony.client.systems.assets.GuiAssetSystem;
import net.lapidist.colony.client.systems.assets.MapAssetSystem;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;
import net.lapidist.colony.core.systems.physics.MapPhysicsSystem;
import net.lapidist.colony.client.systems.render.ClearScreenSystem;
import net.lapidist.colony.client.systems.render.MapRenderSystem;

public class MapScreen implements Screen {

    private World world;

    public MapScreen() {
        MessageManager.getInstance().setDebugEnabled(true);

        try {
            ClientSystem clientSystem = new ClientSystem("127.0.0.1", 9966);
            clientSystem.state = ClientSystem.ClientState.CONNECTED;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new SuperMapper(),
                        new TagManager(),
                        new MapAssetSystem(FileLocation.INTERNAL),
                        new EntityFactorySystem(),
                        new LightFactorySystem()
                )
                .with(WorldConfigurationBuilder.Priority.NORMAL,
                        new ClearScreenSystem(Color.BLACK),
                        new TimeSystem(),
                        new GuiAssetSystem(FileLocation.INTERNAL),
                        new PlayerCameraSystem(1),
                        new TerrainGeneratorSystem("testing", 25, 25),
                        new MapGeneratorSystem(
                                42,
                                42,
                                Constants.PPM,
                                Constants.PPM,
                                Constants.CHUNK_SIZE,
                                Constants.CHUNK_SIZE
                        ),
                        new MapRenderSystem(),
                        new MapPhysicsSystem(),
                        new GuiRenderSystem(),
                        new PlayerControlSystem()
                )
                .build();
        world = new World(config);
    }

    @Override
    public void render(final float delta) {
        world.setDelta(delta);
        world.process();
        MessageManager.getInstance().update();
    }

    @Override
    public void resize(final int width, final int height) {
        MessageManager.getInstance().dispatchMessage(0, null, Events.RESIZE);
    }

    @Override
    public void pause() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.PAUSE);
    }

    @Override
    public void resume() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.RESUME);
    }

    @Override
    public void hide() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.HIDE);
    }

    @Override
    public void show() {
        MessageManager.getInstance().dispatchMessage(0, null, Events.SHOW);
    }

    @Override
    public void dispose() {
        world.dispose();
        MessageManager.getInstance().clear();
    }
}
