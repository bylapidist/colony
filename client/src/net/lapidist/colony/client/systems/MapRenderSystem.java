package net.lapidist.colony.client.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
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

public final class MapRenderSystem extends BaseSystem {

    private final ResourceLoader resourceLoader = new ResourceLoader();

    private final SpriteBatch spriteBatch = new SpriteBatch();

    private Entity map;

    private PlayerCameraSystem cameraSystem;

    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<BuildingComponent> buildingMapper;

    public MapRenderSystem() {
    }

    @Override
    public void initialize() {
        try {
            resourceLoader.load(FileLocation.INTERNAL, "resources.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        cameraSystem = world.getSystem(PlayerCameraSystem.class);
        mapMapper = world.getMapper(MapComponent.class);
        tileMapper = world.getMapper(TileComponent.class);
        buildingMapper = world.getMapper(BuildingComponent.class);

        IntBag maps = world.getAspectSubscriptionManager()
                .get(Aspect.all(MapComponent.class))
                .getEntities();
        if (maps.size() > 0) {
            map = world.getEntity(maps.get(0));
        }
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        spriteBatch.dispose();
    }

    @Override
    protected void processSystem() {
        spriteBatch.setProjectionMatrix(cameraSystem.getCamera().combined);
        spriteBatch.begin();

        drawEntities(mapMapper.get(map).getTiles());
        drawEntities(mapMapper.get(map).getEntities());

        spriteBatch.end();
    }

    private void drawEntities(final Array<Entity> entities) {
        for (int i = 0; i < entities.size; i++) {
            final Entity entity = entities.get(i);
            final TextureRegionReferenceComponent textureRegionReferenceComponent =
                    world.getMapper(TextureRegionReferenceComponent.class).get(entity);
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
