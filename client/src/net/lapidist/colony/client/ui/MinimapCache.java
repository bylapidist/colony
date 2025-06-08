package net.lapidist.colony.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

/**
 * Handles minimap tile caching.
 */
final class MinimapCache implements Disposable {

    private SpriteCache spriteCache;
    private int cacheId;
    private int cacheWidth;
    private int cacheHeight;
    private boolean invalidated = true;
    private float viewportWidth;
    private float viewportHeight;

    void invalidate() {
        invalidated = true;
    }

    void setViewport(final float width, final float height) {
        this.viewportWidth = width;
        this.viewportHeight = height;
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    void ensureCache(
            final ResourceLoader resourceLoader,
            final Entity map,
            final ComponentMapper<MapComponent> mapMapper,
            final ComponentMapper<TileComponent> tileMapper,
            final ComponentMapper<TextureRegionReferenceComponent> textureMapper,
            final float scaleX,
            final float scaleY
    ) {
        if (spriteCache != null && !invalidated
                && cacheWidth == (int) viewportWidth && cacheHeight == (int) viewportHeight) {
            return;
        }
        if (spriteCache != null) {
            spriteCache.dispose();
        }
        spriteCache = new SpriteCache();
        spriteCache.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, viewportWidth, viewportHeight));
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
        cacheWidth = (int) viewportWidth;
        cacheHeight = (int) viewportHeight;
        invalidated = false;
    }

    void draw(final SpriteBatch batch, final float x, final float y) {
        if (spriteCache == null) {
            return;
        }
        Matrix4 oldProj = spriteCache.getProjectionMatrix().cpy();
        Matrix4 oldTrans = spriteCache.getTransformMatrix().cpy();
        batch.end();
        spriteCache.setProjectionMatrix(batch.getProjectionMatrix());
        spriteCache.setTransformMatrix(new Matrix4().setToTranslation(x, y, 0));
        spriteCache.begin();
        spriteCache.draw(cacheId);
        spriteCache.end();
        batch.begin();
        spriteCache.setProjectionMatrix(oldProj);
        spriteCache.setTransformMatrix(oldTrans);
    }

    @Override
    public void dispose() {
        if (spriteCache != null) {
            spriteCache.dispose();
        }
    }
}
