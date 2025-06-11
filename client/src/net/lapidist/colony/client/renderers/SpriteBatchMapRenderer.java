package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.MapRenderData;

/**
 * SpriteBatch backed implementation of {@link MapRenderer}.
 */
public final class SpriteBatchMapRenderer implements MapRenderer, Disposable {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final TileRenderer tileRenderer;
    private final BuildingRenderer buildingRenderer;
    private final ResourceRenderer resourceRenderer;

    public SpriteBatchMapRenderer(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final TileRenderer tileRendererToSet,
            final BuildingRenderer buildingRendererToSet,
            final ResourceRenderer resourceRendererToSet
    ) {
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.tileRenderer = tileRendererToSet;
        this.buildingRenderer = buildingRendererToSet;
        this.resourceRenderer = resourceRendererToSet;
    }

    @Override
    public void render(final MapRenderData map, final CameraProvider camera) {
        spriteBatch.setProjectionMatrix(camera.getCamera().combined);
        spriteBatch.begin();

        tileRenderer.render(map.getTiles());
        buildingRenderer.render(map.getBuildings());
        resourceRenderer.render(map.getTiles());

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        resourceRenderer.dispose();
        spriteBatch.dispose();
    }
}
