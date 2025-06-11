package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderTile;

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
    public void render(final Array<RenderTile> entities) {
        Vector2 worldCoords = new Vector2();
        Vector3 tmp = new Vector3();
        for (int i = 0; i < entities.size; i++) {
            RenderTile tile = entities.get(i);
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
