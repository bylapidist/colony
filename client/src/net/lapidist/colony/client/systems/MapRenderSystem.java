package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import java.io.IOException;

import static net.lapidist.colony.client.entities.Mappers.MAPS;
import static net.lapidist.colony.client.entities.Mappers.TEXTURE_REGIONS;

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
            resourceLoader.load(
                    FileLocation.INTERNAL,
                    "resources.json"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        cameraSystem = engine.getSystem(PlayerCameraSystem.class);

        map = engine.getEntitiesFor(
                Family.one(MapComponent.class).get()
        ).first();
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

        drawTiles();

        spriteBatch.end();
    }

    private void drawTiles() {
        MapComponent mapComponent = MAPS.get(map);

        for (int i = 0; i < mapComponent.getTiles().size; ++i) {
            Entity entity = mapComponent.getTiles().get(i);
            TileComponent tile = entity.getComponent(TileComponent.class);
            Vector2 worldCoords = cameraSystem.tileCoordsToWorldCoords(tile.getX(), tile.getY());

            if (cameraSystem.withinCameraView(worldCoords)) {
                TextureRegionReferenceComponent textureRegionReferenceComponent =
                        TEXTURE_REGIONS.get(entity);

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
}
