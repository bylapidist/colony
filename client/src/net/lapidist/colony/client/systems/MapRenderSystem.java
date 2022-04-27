package net.lapidist.colony.client.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.entities.TileComponent;
import java.io.IOException;

import static net.lapidist.colony.client.entities.Mappers.TEXTURE_REGIONS;

public class MapRenderSystem extends EntitySystem {

    private final ResourceLoader resourceLoader = new ResourceLoader();

    private final SpriteBatch spriteBatch = new SpriteBatch();

    private ImmutableArray<Entity> tiles;

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

        tiles = engine.getEntitiesFor(
                Family.all(TileComponent.class).get()
        );
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
        for (int i = 0; i < tiles.size(); ++i) {
            Entity entity = tiles.get(i);
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
