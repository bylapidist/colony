package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.MapRenderData;

/**
 * Renders building entities.
 */
public final class BuildingRenderer implements EntityRenderer<RenderBuilding> {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final AssetResolver resolver;
    private final java.util.Map<String, TextureRegion> regionCache = new java.util.HashMap<>();

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

    @Override
    public void render(final MapRenderData map) {
        Array<RenderBuilding> entities = map.getBuildings();
        Vector2 worldCoords = new Vector2();
        Vector3 tmp = new Vector3();
        for (int i = 0; i < entities.size; i++) {
            RenderBuilding building = entities.get(i);
            CameraUtils.tileCoordsToWorldCoords(building.getX(), building.getY(), worldCoords);

            if (!CameraUtils.withinCameraView(cameraSystem.getViewport(), worldCoords, tmp)) {
                continue;
            }

            String ref = resolver.buildingAsset(building.getBuildingType());
            TextureRegion region = regionCache.computeIfAbsent(ref, resourceLoader::findRegion);
            if (region != null) {
                spriteBatch.draw(region, worldCoords.x, worldCoords.y);
            }
        }
    }
}
