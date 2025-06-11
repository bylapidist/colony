package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.components.GameConstants;

/**
 * Renders tile entities.
 */
public final class TileRenderer implements EntityRenderer<RenderTile> {

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

    @Override
    public void render(final MapRenderData map) {
        Rectangle view = CameraUtils.getViewBounds(
                (com.badlogic.gdx.graphics.OrthographicCamera) cameraSystem.getCamera(),
                (com.badlogic.gdx.utils.viewport.ExtendViewport) cameraSystem.getViewport(),
                new Rectangle()
        );
        Vector2 start = CameraUtils.worldCoordsToTileCoords((int) view.x, (int) view.y);
        Vector2 end = CameraUtils.worldCoordsToTileCoords(
                (int) (view.x + view.width),
                (int) (view.y + view.height)
        );

        int startX = Math.max(0, (int) start.x - 1);
        int startY = Math.max(0, (int) start.y - 1);
        int endX = Math.min(GameConstants.MAP_WIDTH - 1, (int) end.x + 1);
        int endY = Math.min(GameConstants.MAP_HEIGHT - 1, (int) end.y + 1);

        Vector2 worldCoords = new Vector2();
        Vector3 tmp = new Vector3();

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                RenderTile tile = map.getTile(x, y);
                if (tile == null) {
                    continue;
                }

                CameraUtils.tileCoordsToWorldCoords(tile.getX(), tile.getY(), worldCoords);
                if (!CameraUtils.withinCameraView(cameraSystem.getViewport(), worldCoords, tmp)) {
                    continue;
                }

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
