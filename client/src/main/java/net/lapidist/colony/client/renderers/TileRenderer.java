package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.MapRenderData;

/**
 * Renders tile entities.
 */
public final class TileRenderer {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final AssetResolver resolver;

    public TileRenderer(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final CameraProvider cameraSystemToSet,
            final AssetResolver resolverToSet
    ) {
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.cameraSystem = cameraSystemToSet;
        this.resolver = resolverToSet;
    }

    public void render(final MapRenderData map) {
        Vector2 worldCoords = new Vector2();
        Rectangle bounds = cameraSystem.getVisibleTileBounds();
        int startX = (int) bounds.x;
        int startY = (int) bounds.y;
        int endX = (int) (bounds.x + bounds.width);
        int endY = (int) (bounds.y + bounds.height);

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                RenderTile tile = map.getTile(x, y);
                if (tile == null) {
                    continue;
                }

                CameraUtils.tileCoordsToWorldCoords(tile.getX(), tile.getY(), worldCoords);
                String ref = resolver.tileAsset(tile.getTileType());
                TextureRegion region = resourceLoader.findRegion(ref);
                if (region != null) {
                    spriteBatch.draw(region, worldCoords.x, worldCoords.y);
                }

                if (tile.isSelected()) {
                    TextureRegion overlay = resourceLoader.findRegion("hoveredTile0");
                    if (overlay != null) {
                        spriteBatch.draw(overlay, worldCoords.x, worldCoords.y);
                    }
                }
            }
        }
    }
}
