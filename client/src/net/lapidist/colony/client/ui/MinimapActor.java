package net.lapidist.colony.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private ShapeRenderer shapeRenderer;
    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<TextureRegionReferenceComponent> textureMapper;
    private Entity map;
    private PlayerCameraSystem cameraSystem;
    private float mapWidthWorld;
    private float mapHeightWorld;
    private SpriteCache spriteCache;
    private int cacheId;
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
            map = net.lapidist.colony.map.MapUtils.findMapEntity(world);
        }
    }

    private void updateCache(final float scaleX, final float scaleY) {
        if (spriteCache != null) {
            spriteCache.dispose();
        }

        spriteCache = new SpriteCache();
        spriteCache.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, getWidth(), getHeight()));
        spriteCache.beginCache();

        Array<Entity> tiles = mapMapper.get(map).getTiles();
        for (int i = 0; i < tiles.size; i++) {
            Entity tile = tiles.get(i);
            TileComponent tileComponent = tileMapper.get(tile);
            TextureRegion region = resourceLoader.findRegion(
                    textureMapper.get(tile).getResourceRef());
            if (region != null) {
                spriteCache.add(
                        region,
                        tileComponent.getX() * Constants.TILE_SIZE * scaleX,
                        tileComponent.getY() * Constants.TILE_SIZE * scaleY,
                        Constants.TILE_SIZE * scaleX,
                        Constants.TILE_SIZE * scaleY
                );
            }
        }

        cacheId = spriteCache.endCache();
        cacheInvalidated = false;
    }

    public MinimapActor(final World worldToSet) {
        this.world = worldToSet;
        // ShapeRenderer will be used to draw the viewport rectangle when GL is available
        setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        try {
            resourceLoader.load(FileLocation.INTERNAL, "textures/textures.atlas");
        } catch (IOException e) {
            // ignore loading errors in headless tests
        }
        if (Gdx.app != null && Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.HeadlessDesktop) {
            shapeRenderer = new ShapeRenderer();
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

        if (spriteCache == null || cacheInvalidated) {
            updateCache(scaleX, scaleY);
        }

        if (spriteCache != null) {
            SpriteBatch sb = (SpriteBatch) batch;
            sb.flush();
            spriteCache.setProjectionMatrix(sb.getProjectionMatrix());
            Matrix4 oldTransform = new Matrix4(spriteCache.getTransformMatrix());
            spriteCache.setTransformMatrix(new Matrix4().setToTranslation(getX(), getY(), 0));
            spriteCache.begin();
            spriteCache.draw(cacheId);
            spriteCache.end();
            spriteCache.setTransformMatrix(oldTransform);
        }

        if (cameraSystem != null) {
            com.badlogic.gdx.math.Rectangle view = cameraSystem.getViewBounds();
            Vector2 bottomLeft = new Vector2(view.x, view.y);
            Vector2 topRight = new Vector2(view.x + view.width, view.y + view.height);

            float clampedLeft = Math.max(0, bottomLeft.x);
            float clampedBottom = Math.max(0, bottomLeft.y);
            float clampedRight = Math.min(mapWidthWorld, topRight.x);
            float clampedTop = Math.min(mapHeightWorld, topRight.y);

            float rectX = getX() + clampedLeft * scaleX;
            float rectY = getY() + clampedBottom * scaleY;
            float rectWidth = (clampedRight - clampedLeft) * scaleX;
            float rectHeight = (clampedTop - clampedBottom) * scaleY;
            if (shapeRenderer != null) {
                batch.end();
                shapeRenderer.setProjectionMatrix(((SpriteBatch) batch).getProjectionMatrix());
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(rectX, rectY, rectWidth, rectHeight);
                shapeRenderer.end();
                batch.begin();
            }
        }
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (spriteCache != null) {
            spriteCache.dispose();
        }
    }
}
