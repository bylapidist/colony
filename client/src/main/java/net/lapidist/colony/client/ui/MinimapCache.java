package net.lapidist.colony.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.renderers.AssetResolver;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

/**
 * Handles minimap tile caching.
 */
final class MinimapCache implements Disposable {

    private static final int MAX_SPRITES_PER_CACHE = 8191;

    private final Array<SpriteCache> spriteCaches = new Array<>();
    private final IntArray cacheIds = new IntArray();
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
            final float scaleX,
            final float scaleY
    ) {
        if (!spriteCaches.isEmpty() && !invalidated
                && cacheWidth == (int) viewportWidth && cacheHeight == (int) viewportHeight) {
            return;
        }
        dispose();
        Array<Entity> tiles = mapMapper.get(map).getTiles();
        AssetResolver resolver = new DefaultAssetResolver();
        for (int start = 0; start < tiles.size; start += MAX_SPRITES_PER_CACHE) {
            int count = Math.min(MAX_SPRITES_PER_CACHE, tiles.size - start);
            SpriteCache cache = new SpriteCache(count, true);
            cache.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, viewportWidth, viewportHeight));
            cache.beginCache();
            for (int i = 0; i < count; i++) {
                Entity tile = tiles.get(start + i);
                TileComponent tileComponent = tileMapper.get(tile);
                TextureRegion region = resourceLoader.findRegion(
                        resolver.tileAsset(tileComponent.getTileType().toString()));
                if (region != null) {
                    cache.add(
                            region,
                            tileComponent.getX() * GameConstants.TILE_SIZE * scaleX,
                            tileComponent.getY() * GameConstants.TILE_SIZE * scaleY,
                            GameConstants.TILE_SIZE * scaleX,
                            GameConstants.TILE_SIZE * scaleY
                    );
                }
            }
            cacheIds.add(cache.endCache());
            spriteCaches.add(cache);
        }

        cacheWidth = (int) viewportWidth;
        cacheHeight = (int) viewportHeight;
        invalidated = false;
    }

    void draw(final SpriteBatch batch, final float x, final float y) {
        if (spriteCaches.isEmpty()) {
            return;
        }
        Matrix4 oldProj = spriteCaches.first().getProjectionMatrix().cpy();
        Matrix4 oldTrans = spriteCaches.first().getTransformMatrix().cpy();
        batch.end();
        for (int i = 0; i < spriteCaches.size; i++) {
            SpriteCache cache = spriteCaches.get(i);
            cache.setProjectionMatrix(batch.getProjectionMatrix());
            cache.setTransformMatrix(new Matrix4().setToTranslation(x, y, 0));
            cache.begin();
            cache.draw(cacheIds.get(i));
            cache.end();
        }
        batch.begin();
        for (SpriteCache cache : spriteCaches) {
            cache.setProjectionMatrix(oldProj);
            cache.setTransformMatrix(oldTrans);
        }
    }

    @Override
    public void dispose() {
        for (SpriteCache cache : spriteCaches) {
            cache.dispose();
        }
        spriteCaches.clear();
        cacheIds.clear();
    }
}
