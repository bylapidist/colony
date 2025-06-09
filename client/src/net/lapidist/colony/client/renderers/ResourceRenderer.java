package net.lapidist.colony.client.renderers;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import com.badlogic.gdx.utils.Disposable;

/** Draws resource amounts on tiles. */
public final class ResourceRenderer implements EntityRenderer, Disposable {
    private final SpriteBatch spriteBatch;
    private final PlayerCameraSystem cameraSystem;
    private final BitmapFont font = new BitmapFont();
    private final ComponentMapper<TileComponent> tileMapper;
    private final ComponentMapper<ResourceComponent> resourceMapper;
    private static final float OFFSET_Y = 8f;

    public ResourceRenderer(
            final SpriteBatch spriteBatchToUse,
            final PlayerCameraSystem cameraSystemToUse,
            final ComponentMapper<TileComponent> tileMapperToUse,
            final ComponentMapper<ResourceComponent> resourceMapperToUse
    ) {
        this.spriteBatch = spriteBatchToUse;
        this.cameraSystem = cameraSystemToUse;
        this.tileMapper = tileMapperToUse;
        this.resourceMapper = resourceMapperToUse;
    }

    @Override
    public void render(final Array<Entity> entities) {
        for (int i = 0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            TileComponent tile = tileMapper.get(entity);
            Vector2 worldCoords = CameraUtils.tileCoordsToWorldCoords(tile.getX(), tile.getY());
            if (!CameraUtils.withinCameraView(cameraSystem.getViewport(), worldCoords)) {
                continue;
            }
            ResourceComponent rc = resourceMapper.get(entity);
            String text = rc.getWood() + "/" + rc.getStone() + "/" + rc.getFood();
            font.draw(spriteBatch, text, worldCoords.x, worldCoords.y + OFFSET_Y);
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
