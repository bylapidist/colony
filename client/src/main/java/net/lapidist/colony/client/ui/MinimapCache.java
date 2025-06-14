package net.lapidist.colony.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

/**
 * Handles minimap tile caching.
 */
final class MinimapCache implements Disposable {

    private static final Color GRASS_COLOR = new Color(0.3f, 0.8f, 0.3f, 1f);
    private static final Color DIRT_COLOR = new Color(0.55f, 0.27f, 0.07f, 1f);

    private Texture texture;
    private Pixmap pixmap;
    private int cacheWidth;
    private int cacheHeight;
    private boolean invalidated = true;
    private float viewportWidth;
    private float viewportHeight;
    private int mapWidth;
    private int mapHeight;

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
        if (texture != null && !invalidated
                && cacheWidth == (int) viewportWidth && cacheHeight == (int) viewportHeight) {
            return;
        }

        dispose();

        Array<Entity> tiles = mapMapper.get(map).getTiles();
        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < tiles.size; i++) {
            TileComponent tc = tileMapper.get(tiles.get(i));
            maxX = Math.max(maxX, tc.getX());
            maxY = Math.max(maxY, tc.getY());
        }
        mapWidth = Math.max(1, maxX + 1);
        mapHeight = Math.max(1, maxY + 1);

        pixmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888);
        for (int i = 0; i < tiles.size; i++) {
            TileComponent tc = tileMapper.get(tiles.get(i));
            Color c = switch (tc.getTileType()) {
                case "GRASS" -> GRASS_COLOR;
                case "DIRT" -> DIRT_COLOR;
                case "EMPTY" -> Color.CLEAR;
                default -> Color.CLEAR;
            };
            this.pixmap.setColor(c);
            this.pixmap.drawPixel(tc.getX(), mapHeight - 1 - tc.getY());
        }

        texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        cacheWidth = (int) viewportWidth;
        cacheHeight = (int) viewportHeight;
        invalidated = false;
    }

    void draw(final SpriteBatch batch, final float x, final float y) {
        if (texture == null) {
            return;
        }
        batch.draw(texture, x, y, cacheWidth, cacheHeight);
    }

    Texture getTexture() {
        return texture;
    }

    Pixmap getPixmap() {
        return pixmap;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
        if (pixmap != null) {
            pixmap.dispose();
            pixmap = null;
        }
    }
}
