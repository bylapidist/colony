package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.MapRenderData;

/**
 * Renders building entities.
 */
public final class BuildingRenderer {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final AssetResolver resolver;

    public BuildingRenderer(
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
                RenderBuilding building = map.getBuilding(x, y);
                if (building == null) {
                    continue;
                }

                CameraUtils.tileCoordsToWorldCoords(building.getX(), building.getY(), worldCoords);

                String ref = resolver.buildingAsset(building.getBuildingType());
                TextureRegion region = resourceLoader.findRegion(ref);
                if (region != null) {
                    spriteBatch.draw(region, worldCoords.x, worldCoords.y);
                }
            }
        }
    }
}
