package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderTile;
import com.badlogic.gdx.utils.Disposable;

/** Draws resource amounts on tiles. */
public final class ResourceRenderer implements EntityRenderer<RenderTile>, Disposable {
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

    @Override
    public void render(final Array<RenderTile> entities) {
        Vector2 worldCoords = new Vector2();
        Vector3 tmp = new Vector3();
        for (int i = 0; i < entities.size; i++) {
            RenderTile tile = entities.get(i);
            CameraUtils.tileCoordsToWorldCoords(tile.getX(), tile.getY(), worldCoords);
            if (!CameraUtils.withinCameraView(cameraSystem.getViewport(), worldCoords, tmp)) {
                continue;
            }
            String text = tile.getWood() + "/" + tile.getStone() + "/" + tile.getFood();
            font.draw(spriteBatch, text, worldCoords.x, worldCoords.y + OFFSET_Y);
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
