package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.util.CameraUtils;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import com.badlogic.gdx.utils.Disposable;

/** Draws resource amounts on tiles. */
public final class ResourceRenderer implements EntityRenderer<RenderTile>, Disposable {
    private final SpriteBatch spriteBatch;
    private final CameraProvider cameraSystem;
    private final MapRenderDataSystem dataSystem;
    private final BitmapFont font = new BitmapFont();
    private final GlyphLayout layout = new GlyphLayout();
    private final StringBuilder textBuilder = new StringBuilder();
    private static final float OFFSET_Y = 8f;

    public ResourceRenderer(
            final SpriteBatch spriteBatchToUse,
            final CameraProvider cameraSystemToUse,
            final MapRenderDataSystem dataSystemToUse
    ) {
        this.spriteBatch = spriteBatchToUse;
        this.cameraSystem = cameraSystemToUse;
        this.dataSystem = dataSystemToUse;
    }

    @Override
    public void render(final MapRenderData map) {
        Array<RenderTile> entities = map.getTiles();
        Vector2 worldCoords = new Vector2();
        Vector3 tmp = new Vector3();
        com.badlogic.gdx.utils.IntArray indices = dataSystem.getSelectedTileIndices();
        for (int j = 0; j < indices.size; j++) {
            int i = indices.get(j);
            if (i < 0 || i >= entities.size) {
                continue;
            }
            RenderTile tile = entities.get(i);
            CameraUtils.tileCoordsToWorldCoords(tile.getX(), tile.getY(), worldCoords);
            if (!CameraUtils.withinCameraView(cameraSystem.getViewport(), worldCoords, tmp)) {
                continue;
            }
            textBuilder.setLength(0);
            textBuilder
                    .append(tile.getWood())
                    .append('/')
                    .append(tile.getStone())
                    .append('/')
                    .append(tile.getFood());
            layout.setText(font, textBuilder);
            font.draw(spriteBatch, layout, worldCoords.x, worldCoords.y + OFFSET_Y);
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
