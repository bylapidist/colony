package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.BooleanArray;
import net.lapidist.colony.client.util.CameraUtils;
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
    private final Array<Rectangle> cacheBounds = new Array<>();
    private final BooleanArray invalidSegments = new BooleanArray();
    private final Matrix4 oldProj = new Matrix4();
    private final Rectangle viewBounds = new Rectangle();
    private MapRenderData cachedData;
    private int cachedVersion;
    private boolean invalidated = true;

    void invalidate() {
        invalidated = true;
    }

    void invalidateTiles(final IntArray indices) {
        if (indices == null) {
            return;
        }
        for (int i = 0; i < indices.size; i++) {
            int segment = indices.get(i) / MAX_SPRITES_PER_CACHE;
            if (segment >= invalidSegments.size) {
                continue;
            }
            invalidSegments.set(segment, true);
        }
    }

    void ensureCache(
            final ResourceLoader loader,
            final MapRenderData map,
            final AssetResolver resolver,
            final CameraProvider camera
    ) {
        if (map == null) {
            dispose();
            return;
        }

        if (spriteCaches.isEmpty() || cachedData != map) {
            dispose();
            rebuildAll(loader, map, resolver, camera);
            return;
        }

        boolean anyInvalid = invalidated;
        for (int i = 0; i < invalidSegments.size && !anyInvalid; i++) {
            if (invalidSegments.get(i)) {
                anyInvalid = true;
            }
        }

        if (!anyInvalid && cachedVersion == map.getVersion()) {
            return;
        }

        if (invalidated) {
            dispose();
            rebuildAll(loader, map, resolver, camera);
            return;
        }

        Array<RenderTile> tiles = map.getTiles();
        for (int segment = 0, start = 0; start < tiles.size; segment++, start += MAX_SPRITES_PER_CACHE) {
            int count = Math.min(MAX_SPRITES_PER_CACHE, tiles.size - start);
            if (segment >= invalidSegments.size || !invalidSegments.get(segment)) {
                continue;
            }

            SpriteCache old = spriteCaches.get(segment);
            if (old != null) {
                old.dispose();
            }
            SpriteCache cache = new SpriteCache(count, true);
            cache.setProjectionMatrix(camera.getCamera().combined);
            cache.beginCache();

            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;

            for (int i = 0; i < count; i++) {
                RenderTile tile = tiles.get(start + i);
                String ref = resolver.tileAsset(tile.getTileType());
                TextureRegion region = loader.findRegion(ref);

                float worldX = tile.getX() * GameConstants.TILE_SIZE;
                float worldY = tile.getY() * GameConstants.TILE_SIZE;

                minX = Math.min(minX, worldX);
                minY = Math.min(minY, worldY);
                maxX = Math.max(maxX, worldX + GameConstants.TILE_SIZE);
                maxY = Math.max(maxY, worldY + GameConstants.TILE_SIZE);

                if (region != null) {
                    cache.add(region, worldX, worldY);
                }
            }

            cacheIds.set(segment, cache.endCache());
            spriteCaches.set(segment, cache);
            cacheBounds.set(segment, new Rectangle(minX, minY, maxX - minX, maxY - minY));
            invalidSegments.set(segment, false);
        }

        cachedVersion = map.getVersion();
    }

    private void rebuildAll(
            final ResourceLoader loader,
            final MapRenderData map,
            final AssetResolver resolver,
            final CameraProvider camera
    ) {
        dispose();
        cachedData = map;
        cachedVersion = map.getVersion();
        Array<RenderTile> tiles = map.getTiles();
        for (int start = 0, segment = 0; start < tiles.size; start += MAX_SPRITES_PER_CACHE, segment++) {
            int count = Math.min(MAX_SPRITES_PER_CACHE, tiles.size - start);
            SpriteCache cache = new SpriteCache(count, true);
            cache.setProjectionMatrix(camera.getCamera().combined);
            cache.beginCache();

            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;

            for (int i = 0; i < count; i++) {
                RenderTile tile = tiles.get(start + i);
                String ref = resolver.tileAsset(tile.getTileType());
                TextureRegion region = loader.findRegion(ref);

                float worldX = tile.getX() * GameConstants.TILE_SIZE;
                float worldY = tile.getY() * GameConstants.TILE_SIZE;

                minX = Math.min(minX, worldX);
                minY = Math.min(minY, worldY);
                maxX = Math.max(maxX, worldX + GameConstants.TILE_SIZE);
                maxY = Math.max(maxY, worldY + GameConstants.TILE_SIZE);

                if (region != null) {
                    cache.add(region, worldX, worldY);
                }
            }

            cacheIds.add(cache.endCache());
            spriteCaches.add(cache);
            cacheBounds.add(new Rectangle(minX, minY, maxX - minX, maxY - minY));
            invalidSegments.add(false);
        }

        invalidated = false;
    }

    void draw(final SpriteBatch batch, final CameraProvider camera) {
        if (spriteCaches.isEmpty()) {
            return;
        }
        oldProj.set(spriteCaches.first().getProjectionMatrix());
        batch.end();

        Rectangle view = CameraUtils.getViewBounds(
                (OrthographicCamera) camera.getCamera(),
                (ExtendViewport) camera.getViewport(),
                viewBounds
        );

        for (int i = 0; i < spriteCaches.size; i++) {
            if (!view.overlaps(cacheBounds.get(i))) {
                continue;
            }
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
        cacheBounds.clear();
        invalidSegments.clear();
        cachedData = null;
    }
}
