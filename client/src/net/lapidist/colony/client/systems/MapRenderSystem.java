package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import java.io.IOException;

public class MapRenderSystem extends EntitySystem {

    private final ResourceLoader resourceLoader = new ResourceLoader();

    private final SpriteBatch spriteBatch = new SpriteBatch();

    private Entity map;

    private PlayerCameraSystem cameraSystem;

    public MapRenderSystem() {
    }

    @Override
    public final void addedToEngine(final Engine engine) {
        try {
            resourceLoader.load(FileLocation.INTERNAL, "resources.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        cameraSystem = engine.getSystem(PlayerCameraSystem.class);
        map = engine.getEntitiesFor(Family.one(MapComponent.class).get()).first();
    }

    @Override
    public final void removedFromEngine(final Engine engine) {
        resourceLoader.dispose();
        spriteBatch.dispose();
    }

    @Override
    public final void update(final float deltaTime) {
        cameraSystem.update(deltaTime);
        spriteBatch.setProjectionMatrix(cameraSystem.getCamera().combined);
        spriteBatch.begin();

        drawEntities(map.getComponent(MapComponent.class).getTiles());
        drawEntities(map.getComponent(MapComponent.class).getEntities());

        spriteBatch.end();
    }

    private void drawEntities(final Array<Entity> entities) {
        for (int i = 0; i < entities.size; i++) {
            final Entity entity = entities.get(i);
            final ComponentMapper<TextureRegionReferenceComponent> textureRegionMapper =
                    ComponentMapper.getFor(TextureRegionReferenceComponent.class);
            final TextureRegionReferenceComponent textureRegionReferenceComponent =
                    textureRegionMapper.get(entity);
            final Vector2 worldCoords = getWorldCoords(entity);

            if (cameraSystem.withinCameraView(worldCoords)) {
                spriteBatch.draw(
                        resourceLoader.getTextureRegions().get(
                                textureRegionReferenceComponent.getResourceRef()
                        ),
                        worldCoords.x,
                        worldCoords.y
                );
            }
        }
    }

    private Vector2 getWorldCoords(final Entity entity) {
        final ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);
        final ComponentMapper<BuildingComponent> buildingMapper = ComponentMapper.getFor(BuildingComponent.class);

        if (tileMapper.has(entity)) {
            final TileComponent tileComponent = tileMapper.get(entity);
            return cameraSystem.tileCoordsToWorldCoords(tileComponent.getX(), tileComponent.getY());
        } else if (buildingMapper.has(entity)) {
            final BuildingComponent buildingComponent = buildingMapper.get(entity);
            return cameraSystem.tileCoordsToWorldCoords(buildingComponent.getX(), buildingComponent.getY());
        }

        return Vector2.Zero;
    }
}
