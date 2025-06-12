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
import net.lapidist.colony.components.entities.BuildingComponent;

/**
 * Renders building entities.
 */
public final class BuildingRenderer implements EntityRenderer<RenderBuilding> {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final AssetResolver resolver;
    private final java.util.Map<String, TextureRegion> buildingRegions = new java.util.HashMap<>();

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

        for (BuildingComponent.BuildingType type : BuildingComponent.BuildingType.values()) {
            String ref = resolver.buildingAsset(type.name());
            TextureRegion region = resourceLoader.findRegion(ref);
            if (region != null) {
                buildingRegions.put(type.name(), region);
            }
        }
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

            TextureRegion region = buildingRegions.get(building.getBuildingType().toUpperCase(java.util.Locale.ROOT));
            if (region != null) {
                spriteBatch.draw(region, worldCoords.x, worldCoords.y);
            }
        }
    }
}
