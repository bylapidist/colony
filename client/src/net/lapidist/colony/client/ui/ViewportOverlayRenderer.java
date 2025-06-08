package net.lapidist.colony.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.client.util.CameraUtils;

/**
 * Renders the camera viewport rectangle on the minimap.
 */
final class ViewportOverlayRenderer implements Disposable {

    private final PlayerCameraSystem cameraSystem;
    private ShapeRenderer shapeRenderer;

    ViewportOverlayRenderer(final PlayerCameraSystem system) {
        this.cameraSystem = system;
        if (Gdx.app != null && Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.HeadlessDesktop) {
            shapeRenderer = new ShapeRenderer();
        }
    }

    void render(
            final Batch batch,
            final float mapWidthWorld,
            final float mapHeightWorld,
            final float scaleX,
            final float scaleY,
            final float x,
            final float y
    ) {
        if (cameraSystem == null || shapeRenderer == null) {
            return;
        }
        Rectangle view = CameraUtils.getViewBounds(
                cameraSystem.getCamera(),
                cameraSystem.getViewport(),
                new Rectangle()
        );
        Vector2 bottomLeft = new Vector2(view.x, view.y);
        Vector2 topRight = new Vector2(view.x + view.width, view.y + view.height);

        float clampedLeft = Math.max(0, bottomLeft.x);
        float clampedBottom = Math.max(0, bottomLeft.y);
        float clampedRight = Math.min(mapWidthWorld, topRight.x);
        float clampedTop = Math.min(mapHeightWorld, topRight.y);

        float rectX = x + clampedLeft * scaleX;
        float rectY = y + clampedBottom * scaleY;
        float rectWidth = (clampedRight - clampedLeft) * scaleX;
        float rectHeight = (clampedTop - clampedBottom) * scaleY;

        batch.end();
        shapeRenderer.setProjectionMatrix(((SpriteBatch) batch).getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(rectX, rectY, rectWidth, rectHeight);
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
