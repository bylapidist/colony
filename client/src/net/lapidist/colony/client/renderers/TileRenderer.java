package net.lapidist.colony.client.renderers;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.maps.TileComponent;

/**
 * Renders tile entities.
 */
public final class TileRenderer implements EntityRenderer {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final PlayerCameraSystem cameraSystem;
    private final ComponentMapper<TileComponent> tileMapper;
    private final ComponentMapper<TextureRegionReferenceComponent> textureMapper;

    public TileRenderer(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final PlayerCameraSystem cameraSystemToSet,
            final ComponentMapper<TileComponent> tileMapperToSet,
            final ComponentMapper<TextureRegionReferenceComponent> textureMapperToSet
    ) {
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.cameraSystem = cameraSystemToSet;
        this.tileMapper = tileMapperToSet;
        this.textureMapper = textureMapperToSet;
    }

    @Override
    public void render(final Array<Entity> entities) {
        for (int i = 0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            TileComponent tile = tileMapper.get(entity);
            Vector2 worldCoords = cameraSystem.tileCoordsToWorldCoords(
                    tile.getX(),
                    tile.getY()
            );

            if (!cameraSystem.withinCameraView(worldCoords)) {
                continue;
            }

            TextureRegion region = resourceLoader.findRegion(
                    textureMapper.get(entity).getResourceRef()
            );
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
