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

    private static final Vector2 ORIGIN = new Vector2(512, 512);
    private static final int RADIUS = 128;
    private static final int ANGLE = 0;

    private final PooledEngine pooledEngine = new PooledEngine();
    private final ResourceLoader resourceLoader = new ResourceLoader(
            FileLocation.INTERNAL,
            "resources.json"
    );

    public MapScreen() {
        pooledEngine.addSystem(new ClearScreenSystem(Color.BLACK));
        pooledEngine.addSystem(new OrbitRenderSystem());
        pooledEngine.addSystem(new UISystem());

        pooledEngine.addEntity(createPlanet(
                ORIGIN,
                RADIUS,
                ANGLE
        ));

        pooledEngine.addEntity(createPlanet(
                ORIGIN,
                RADIUS / 2f,
                ANGLE
        ));

        try {
            resourceLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Entity createPlanet(final Vector2 origin, final float radius, final float angle) {
        final Entity planetE = pooledEngine.createEntity();
        final OrbitalRadiusComponent orbitC = pooledEngine.createComponent(
                OrbitalRadiusComponent.class
        );

        orbitC.setOrigin(origin);
        orbitC.setRadius(radius);
        orbitC.setAngle(angle);
        planetE.add(orbitC);

        return planetE;
    }

    @Override
    public final void render(final float deltaTime) {
        Events.update();
        pooledEngine.update(deltaTime);
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
