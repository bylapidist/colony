package net.lapidist.colony.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.core.io.TextureAtlasResourceLoader;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.settings.GraphicsSettings;
import net.lapidist.colony.settings.Settings;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

// Helper classes to reduce branching

import java.io.IOException;

/**
 * Draws a minimap representing the current map.
 */
public final class MinimapActor extends Actor implements Disposable {

    private static final int DEFAULT_SIZE = 128;

    private final World world;
    private final ResourceLoader resourceLoader = new TextureAtlasResourceLoader();
    private ViewportOverlayRenderer overlayRenderer;
    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private Entity map;
    private PlayerCameraSystem cameraSystem;
    private float mapWidthWorld;
    private float mapHeightWorld;
    private final MinimapCache cache = new MinimapCache();
    private boolean cacheInvalidated;

    /**
     * Forces the minimap tiles to be redrawn the next time this actor is drawn.
     */
    public void invalidateCache() {
        cacheInvalidated = true;
        cache.invalidate();
    }

    private void calculateMapDimensions() {
        mapWidthWorld = 0;
        mapHeightWorld = 0;

        Array<Entity> tiles = mapMapper.get(map).getTiles();
        for (int i = 0; i < tiles.size; i++) {
            TileComponent tileComponent = tileMapper.get(tiles.get(i));
            mapWidthWorld = Math.max(mapWidthWorld,
                    (tileComponent.getX() + 1) * GameConstants.TILE_SIZE);
            mapHeightWorld = Math.max(mapHeightWorld,
                    (tileComponent.getY() + 1) * GameConstants.TILE_SIZE);
        }

        if (mapWidthWorld == 0) {
            mapWidthWorld = GameConstants.MAP_WIDTH * GameConstants.TILE_SIZE;
        }
        if (mapHeightWorld == 0) {
            mapHeightWorld = GameConstants.MAP_HEIGHT * GameConstants.TILE_SIZE;
        }
    }

    private void initMappers() {
        if (mapMapper == null) {
            mapMapper = world.getMapper(MapComponent.class);
            tileMapper = world.getMapper(TileComponent.class);
        }

        if (cameraSystem == null) {
            cameraSystem = world.getSystem(PlayerCameraSystem.class);
        }

        if (overlayRenderer == null && cameraSystem != null) {
            overlayRenderer = new ViewportOverlayRenderer(cameraSystem);
        }

        if (map == null) {
            map = net.lapidist.colony.map.MapUtils.findMapEntity(world).orElse(null);
        }
    }


    public MinimapActor(final World worldToSet) {
        this(worldToSet, Settings.load().getGraphicsSettings());
    }

    public MinimapActor(
            final World worldToSet,
            final GraphicsSettings graphicsSettings
    ) {
        this.world = worldToSet;
        setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        try {
            resourceLoader.loadTextures(FileLocation.INTERNAL, "textures/textures.atlas", graphicsSettings);
        } catch (IOException e) {
            // ignore loading errors in headless tests
        }
        mapWidthWorld = -1;
        mapHeightWorld = -1;
        cacheInvalidated = true;
    }

    @Override
    protected void setStage(final com.badlogic.gdx.scenes.scene2d.Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            initMappers();
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        if (!(batch instanceof SpriteBatch)) {
            return;
        }

        initMappers();
        if (map == null) {
            return;
        }

        if (mapWidthWorld < 0) {
            calculateMapDimensions();
        }
        float scaleX = getWidth() / mapWidthWorld;
        float scaleY = getHeight() / mapHeightWorld;

        cache.setViewport(getWidth(), getHeight());
        cache.ensureCache(resourceLoader, map, mapMapper, tileMapper,
                scaleX, scaleY);
        cache.draw((SpriteBatch) batch, getX(), getY());

        if (cameraSystem != null && overlayRenderer != null) {
            overlayRenderer.render(batch, mapWidthWorld, mapHeightWorld, scaleX, scaleY, getX(), getY());
        }
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        if (overlayRenderer != null) {
            overlayRenderer.dispose();
        }
        cache.dispose();
    }
}
