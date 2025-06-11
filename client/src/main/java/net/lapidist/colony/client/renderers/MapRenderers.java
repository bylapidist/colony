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
    private final ResourceRenderer resourceRenderer;

    public MapRenderers(
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

    public ResourceRenderer getResourceRenderer() {
        return resourceRenderer;
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        resourceRenderer.dispose();
        spriteBatch.dispose();
    }
}
