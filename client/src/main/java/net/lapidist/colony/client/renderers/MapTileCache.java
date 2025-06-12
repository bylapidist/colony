package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.components.GameConstants;

/**
 * Caches map tiles using {@link SpriteCache} for faster rendering.
 */
final class MapTileCache implements Disposable {

    private static final int MAX_SPRITES_PER_CACHE = 8191;

    private final Array<SpriteCache> spriteCaches = new Array<>();
    private final IntArray cacheIds = new IntArray();
    private MapRenderData cachedData;
    private boolean invalidated = true;

    void invalidate() {
        invalidated = true;
    }

    void ensureCache(
            final ResourceLoader loader,
            final MapRenderData map,
            final AssetResolver resolver,
            final CameraProvider camera
    ) {
        if (!spriteCaches.isEmpty() && !invalidated && cachedData == map) {
            return;
        }
        dispose();
        cachedData = map;
        if (map == null) {
            return;
        }
        Array<RenderTile> tiles = map.getTiles();
        for (int start = 0; start < tiles.size; start += MAX_SPRITES_PER_CACHE) {
            int count = Math.min(MAX_SPRITES_PER_CACHE, tiles.size - start);
            SpriteCache cache = new SpriteCache(count, true);
            cache.setProjectionMatrix(camera.getCamera().combined);
            cache.beginCache();
            for (int i = 0; i < count; i++) {
                RenderTile tile = tiles.get(start + i);
                String ref = resolver.tileAsset(tile.getTileType());
                TextureRegion region = loader.findRegion(ref);
                if (region != null) {
                    float worldX = tile.getX() * GameConstants.TILE_SIZE;
                    float worldY = tile.getY() * GameConstants.TILE_SIZE;
                    cache.add(region, worldX, worldY);
                }
            }
            cacheIds.add(cache.endCache());
            spriteCaches.add(cache);
        }
        invalidated = false;
    }

    void draw(final SpriteBatch batch) {
        if (spriteCaches.isEmpty()) {
            return;
        }
        Matrix4 oldProj = spriteCaches.first().getProjectionMatrix().cpy();
        batch.end();
        for (int i = 0; i < spriteCaches.size; i++) {
            SpriteCache cache = spriteCaches.get(i);
            cache.setProjectionMatrix(batch.getProjectionMatrix());
            cache.begin();
            cache.draw(cacheIds.get(i));
            cache.end();
        }
        batch.begin();
        for (SpriteCache cache : spriteCaches) {
            cache.setProjectionMatrix(oldProj);
        }
    }

    @Override
    public void dispose() {
        for (SpriteCache cache : spriteCaches) {
            cache.dispose();
        }
        spriteCaches.clear();
        cacheIds.clear();
        cachedData = null;
    }
}
