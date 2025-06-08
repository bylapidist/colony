package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.io.ResourceLoader;

/**
 * Holds renderer resources used by {@link net.lapidist.colony.client.systems.MapRenderSystem}.
 */
public final class MapRenderers implements Disposable {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final TileRenderer tileRenderer;
    private final BuildingRenderer buildingRenderer;

    public MapRenderers(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final TileRenderer tileRendererToSet,
            final BuildingRenderer buildingRendererToSet
    ) {
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.tileRenderer = tileRendererToSet;
        this.buildingRenderer = buildingRendererToSet;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public TileRenderer getTileRenderer() {
        return tileRenderer;
    }

    public BuildingRenderer getBuildingRenderer() {
        return buildingRenderer;
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        spriteBatch.dispose();
    }
}
