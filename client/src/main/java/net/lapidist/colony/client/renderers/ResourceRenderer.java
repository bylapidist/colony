package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.MapRenderData;
import com.badlogic.gdx.utils.Disposable;

/** Draws resource amounts on tiles. */
public final class ResourceRenderer implements Disposable {
    private final SpriteBatch spriteBatch;
    private final CameraProvider cameraSystem;
    private final BitmapFont font = new BitmapFont();
    private static final float OFFSET_Y = 8f;

    public ResourceRenderer(
            final SpriteBatch spriteBatchToUse,
            final CameraProvider cameraSystemToUse
    ) {
        this.spriteBatch = spriteBatchToUse;
        this.cameraSystem = cameraSystemToUse;
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
                RenderTile tile = map.getTile(x, y);
                if (tile == null) {
                    continue;
                }

                CameraUtils.tileCoordsToWorldCoords(tile.getX(), tile.getY(), worldCoords);
                String text = tile.getWood() + "/" + tile.getStone() + "/" + tile.getFood();
                font.draw(spriteBatch, text, worldCoords.x, worldCoords.y + OFFSET_Y);
            }
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
