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
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.maps.TileComponent;

/**
 * Renders tile entities.
 */
public final class TileRenderer implements EntityRenderer<RenderTile> {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final AssetResolver resolver;
    private final java.util.Map<String, TextureRegion> tileRegions = new java.util.HashMap<>();
    private final TextureRegion overlayRegion;
    private boolean overlayOnly;

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

        for (TileComponent.TileType type : TileComponent.TileType.values()) {
            String ref = resolver.tileAsset(type.name());
            TextureRegion region = resourceLoader.findRegion(ref);
            if (region != null) {
                tileRegions.put(type.name(), region);
            }
        }
        this.overlayRegion = resourceLoader.findRegion("hoveredTile0");
    }

    public void setOverlayOnly(final boolean overlay) {
        this.overlayOnly = overlay;
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

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                RenderTile tile = map.getTile(x, y);
                if (tile == null) {
                    continue;
                }

                CameraUtils.tileCoordsToWorldCoords(tile.getX(), tile.getY(), worldCoords);

                if (!overlayOnly) {
                    TextureRegion region = tileRegions.get(tile.getTileType().toUpperCase(java.util.Locale.ROOT));
                    if (region != null) {
                        spriteBatch.draw(region, worldCoords.x, worldCoords.y);
                    }
                }

                if (tile.isSelected() && overlayRegion != null) {
                    spriteBatch.draw(overlayRegion, worldCoords.x, worldCoords.y);
                }
            }
        }
    }
}
