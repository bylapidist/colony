package net.lapidist.colony.client.renderers;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.entities.BuildingComponent;

/**
 * Renders building entities.
 */
public final class BuildingRenderer implements EntityRenderer {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final ComponentMapper<BuildingComponent> buildingMapper;
    private final ComponentMapper<TextureRegionReferenceComponent> textureMapper;

    public BuildingRenderer(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final CameraProvider cameraSystemToSet,
            final ComponentMapper<BuildingComponent> buildingMapperToSet,
            final ComponentMapper<TextureRegionReferenceComponent> textureMapperToSet
    ) {
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.cameraSystem = cameraSystemToSet;
        this.buildingMapper = buildingMapperToSet;
        this.textureMapper = textureMapperToSet;
    }

    @Override
    public void render(final Array<Entity> entities) {
        for (int i = 0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            BuildingComponent building = buildingMapper.get(entity);
            Vector2 worldCoords = CameraUtils.tileCoordsToWorldCoords(
                    building.getX(),
                    building.getY()
            );

            if (!CameraUtils.withinCameraView(cameraSystem.getViewport(), worldCoords)) {
                continue;
            }

            TextureRegion region = resourceLoader.findRegion(
                    textureMapper.get(entity).getResourceRef()
            );
            if (region != null) {
                spriteBatch.draw(region, worldCoords.x, worldCoords.y);
            }
        }
    }
}
