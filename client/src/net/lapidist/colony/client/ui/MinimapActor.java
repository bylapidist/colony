package net.lapidist.colony.client.ui;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

import java.io.IOException;

/**
 * Draws a minimap representing the current map.
 */
public final class MinimapActor extends Actor implements Disposable {

    private static final int DEFAULT_SIZE = 128;

    private final World world;
    private final ResourceLoader resourceLoader = new ResourceLoader();
    private final TextureRegion viewportPixel;
    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<TextureRegionReferenceComponent> textureMapper;
    private Entity map;
    private PlayerCameraSystem cameraSystem;
    private float mapWidthWorld;
    private float mapHeightWorld;
    private FrameBuffer tileCache;
    private TextureRegion minimap;
    private SpriteBatch cacheBatch;
    private boolean cacheInvalidated;

    /**
     * Forces the minimap tiles to be redrawn the next time this actor is drawn.
     */
    public void invalidateCache() {
        cacheInvalidated = true;
    }

    private void calculateMapDimensions() {
        mapWidthWorld = 0;
        mapHeightWorld = 0;

        Array<Entity> tiles = mapMapper.get(map).getTiles();
        for (int i = 0; i < tiles.size; i++) {
            TileComponent tileComponent = tileMapper.get(tiles.get(i));
            mapWidthWorld = Math.max(mapWidthWorld,
                    (tileComponent.getX() + 1) * Constants.TILE_SIZE);
            mapHeightWorld = Math.max(mapHeightWorld,
                    (tileComponent.getY() + 1) * Constants.TILE_SIZE);
        }

        if (mapWidthWorld == 0) {
            mapWidthWorld = Constants.MAP_WIDTH * Constants.TILE_SIZE;
        }
        if (mapHeightWorld == 0) {
            mapHeightWorld = Constants.MAP_HEIGHT * Constants.TILE_SIZE;
        }
    }

    private void initMappers() {
        if (mapMapper == null) {
            mapMapper = world.getMapper(MapComponent.class);
            tileMapper = world.getMapper(TileComponent.class);
            textureMapper = world.getMapper(TextureRegionReferenceComponent.class);
        }

        if (cameraSystem == null) {
            cameraSystem = world.getSystem(PlayerCameraSystem.class);
        }

        if (map == null) {
            IntBag maps = world.getAspectSubscriptionManager()
                    .get(Aspect.all(MapComponent.class))
                    .getEntities();
            if (maps.size() > 0) {
                map = world.getEntity(maps.get(0));
            }
        }
    }

    private void updateCache(final float scaleX, final float scaleY) {
        if (tileCache != null) {
            tileCache.dispose();
            tileCache = null;
            minimap = null;
        }

        tileCache = new FrameBuffer(Pixmap.Format.RGBA8888, (int) getWidth(), (int) getHeight(), false);
        if (cacheBatch == null) {
            cacheBatch = new SpriteBatch();
        }

        Matrix4 oldMatrix = cacheBatch.getProjectionMatrix().cpy();
        tileCache.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cacheBatch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, getWidth(), getHeight()));
        cacheBatch.begin();

        Array<Entity> tiles = mapMapper.get(map).getTiles();
        for (int i = 0; i < tiles.size; i++) {
            Entity tile = tiles.get(i);
            TileComponent tileComponent = tileMapper.get(tile);
            TextureRegion region = resourceLoader.getTextureRegions()
                    .get(textureMapper.get(tile).getResourceRef());
            if (region != null) {
                cacheBatch.draw(
                        region,
                        tileComponent.getX() * Constants.TILE_SIZE * scaleX,
                        tileComponent.getY() * Constants.TILE_SIZE * scaleY,
                        Constants.TILE_SIZE * scaleX,
                        Constants.TILE_SIZE * scaleY
                );
            }
        }

        cacheBatch.end();
        tileCache.end();
        cacheBatch.setProjectionMatrix(oldMatrix);
        minimap = new TextureRegion(tileCache.getColorBufferTexture());
        minimap.flip(false, true);
        cacheInvalidated = false;
    }

    public MinimapActor(final World worldToSet) {
        this.world = worldToSet;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        viewportPixel = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();
        setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        try {
            resourceLoader.load(FileLocation.INTERNAL, "resources.conf");
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

        if (minimap == null || cacheInvalidated
                || tileCache == null
                || tileCache.getWidth() != (int) getWidth()
                || tileCache.getHeight() != (int) getHeight()) {
            updateCache(scaleX, scaleY);
        }

        if (minimap != null) {
            batch.draw(minimap, getX(), getY(), getWidth(), getHeight());
        }

        if (cameraSystem != null) {
            Vector2 bottomLeft = cameraSystem.worldCoordsFromCameraCoords(
                    0,
                    0
            );
            Vector2 topRight = cameraSystem.worldCoordsFromCameraCoords(
                    cameraSystem.getViewport().getScreenWidth(),
                    cameraSystem.getViewport().getScreenHeight()
            );

            float clampedLeft = Math.max(0, bottomLeft.x);
            float clampedBottom = Math.max(0, bottomLeft.y);
            float clampedRight = Math.min(mapWidthWorld, topRight.x);
            float clampedTop = Math.min(mapHeightWorld, topRight.y);

            float rectX = getX() + clampedLeft * scaleX;
            float rectY = getY() + clampedBottom * scaleY;
            float rectWidth = (clampedRight - clampedLeft) * scaleX;
            float rectHeight = (clampedTop - clampedBottom) * scaleY;
            float lineWidth = 2f;
            Color old = new Color(batch.getColor());
            batch.setColor(Color.WHITE);
            batch.draw(viewportPixel, rectX, rectY, rectWidth, lineWidth);
            batch.draw(viewportPixel, rectX, rectY + rectHeight - lineWidth, rectWidth, lineWidth);
            batch.draw(viewportPixel, rectX, rectY, lineWidth, rectHeight);
            batch.draw(viewportPixel, rectX + rectWidth - lineWidth, rectY, lineWidth, rectHeight);
            batch.setColor(old);
        }
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        viewportPixel.getTexture().dispose();
        if (tileCache != null) {
            tileCache.dispose();
        }
        if (cacheBatch != null) {
            cacheBatch.dispose();
        }
    }
}
