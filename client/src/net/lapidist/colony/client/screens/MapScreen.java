package net.lapidist.colony.client.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.systems.ClearScreenSystem;
import net.lapidist.colony.client.core.events.EventType;
import net.lapidist.colony.client.core.events.Events;
import net.lapidist.colony.client.core.events.payloads.ResizePayload;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.OrbitRenderSystem;
import net.lapidist.colony.client.systems.UISystem;
import net.lapidist.colony.components.OrbitalRadiusComponent;

import java.io.IOException;

public class MapScreen implements Screen {

    private final PooledEngine pooledEngine = new PooledEngine();
    private final ResourceLoader resourceLoader = new ResourceLoader(
            FileLocation.INTERNAL,
            "resources.json"
    );
    private final Entity planet;
    private final Entity planet2;

    public MapScreen() {
        pooledEngine.addSystem(new ClearScreenSystem(Color.BLACK));
        pooledEngine.addSystem(new OrbitRenderSystem());
        pooledEngine.addSystem(new UISystem());

        planet = pooledEngine.createEntity();
        OrbitalRadiusComponent orbitalRadiusComponent = pooledEngine.createComponent(
                OrbitalRadiusComponent.class
        );
        orbitalRadiusComponent.setRadius(new Vector2(100, 128));
        orbitalRadiusComponent.setOrigin(new Vector2(512, 512));
        orbitalRadiusComponent.setAngle(1);
        planet.add(orbitalRadiusComponent);

        pooledEngine.addEntity(planet);

        planet2 = pooledEngine.createEntity();
        OrbitalRadiusComponent orbitalRadiusComponent2 = pooledEngine.createComponent(
                OrbitalRadiusComponent.class
        );
        orbitalRadiusComponent2.setRadius(new Vector2(50, 64));
        orbitalRadiusComponent2.setOrigin(new Vector2(512, 512));
        orbitalRadiusComponent2.setAngle(1);
        planet2.add(orbitalRadiusComponent2);

        pooledEngine.addEntity(planet2);

        try {
            resourceLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void render(final float deltaTime) {
        Events.update();
        pooledEngine.update(deltaTime);
        planet.getComponent(OrbitalRadiusComponent.class).setAngle(
                planet.getComponent(OrbitalRadiusComponent.class).getAngle() + 0.01f
        );
        planet2.getComponent(OrbitalRadiusComponent.class).setAngle(
                planet2.getComponent(OrbitalRadiusComponent.class).getAngle() + 0.015f
        );
    }

    @Override
    public final void resize(final int width, final int height) {
        Events.dispatch(EventType.RESIZE, new ResizePayload(width, height));
    }

    @Override
    public final void pause() {
        Events.dispatch(EventType.PAUSE);
    }

    @Override
    public final void resume() {
        Events.dispatch(EventType.RESUME);
    }

    @Override
    public final void hide() {
        Events.dispatch(EventType.HIDE);
    }

    @Override
    public final void show() {
        Events.dispatch(EventType.SHOW);
    }

    @Override
    public final void dispose() {
        resourceLoader.dispose();
        Events.dispatch(EventType.DISPOSE);
    }
}
