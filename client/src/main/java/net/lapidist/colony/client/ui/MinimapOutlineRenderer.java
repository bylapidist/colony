package net.lapidist.colony.client.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * Draws a decorative outline around the minimap.
 */
final class MinimapOutlineRenderer implements Disposable {

    private static final float INNER_OFFSET = 1f;
    private static final float OUTER_OFFSET = 2f;

    private ShapeRenderer shapeRenderer;

    MinimapOutlineRenderer() {
        if (Gdx.app != null && Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
            shapeRenderer = new ShapeRenderer();
        }
    }

    void render(
            final Batch batch,
            final float x,
            final float y,
            final float width,
            final float height
    ) {
        if (shapeRenderer == null) {
            return;
        }
        batch.end();
        shapeRenderer.setProjectionMatrix(((SpriteBatch) batch).getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x - INNER_OFFSET, y - INNER_OFFSET,
                width + INNER_OFFSET * 2f, height + INNER_OFFSET * 2f);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x - OUTER_OFFSET, y - OUTER_OFFSET,
                width + OUTER_OFFSET * 2f, height + OUTER_OFFSET * 2f);
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
