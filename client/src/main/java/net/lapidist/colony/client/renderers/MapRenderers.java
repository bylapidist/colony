package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.ParticleSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import java.io.IOException;

/**
 * Holds renderer resources used by {@link net.lapidist.colony.client.systems.MapRenderSystem}.
 */
public final class MapRenderers implements Disposable {

    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final TileRenderer tileRenderer;
    private final BuildingRenderer buildingRenderer;
    private final ResourceRenderer resourceRenderer;
    private ParticleSystem particleSystem;

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

    /** Assign the particle system used for effect playback. */
    public void setParticleSystem(final ParticleSystem system) {
        this.particleSystem = system;
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

    /** Spawn an effect when a building is placed. */
    public void spawnBuildEffect(final float x, final float y) {
        spawnEffect("effects/building_placed.p", x, y);
    }

    /** Spawn an effect when resources are gathered. */
    public void spawnGatherEffect(final float x, final float y) {
        spawnEffect("effects/resource_gather.p", x, y);
    }

    private void spawnEffect(final String path, final float x, final float y) {
        if (particleSystem == null) {
            return;
        }
        try {
            ParticleEffect effect = resourceLoader.loadEffect(path);
            effect.setPosition(x, y);
            particleSystem.spawn(effect);
        } catch (IOException ignored) {
            // ignore missing effect definitions
        }
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        resourceRenderer.dispose();
        spriteBatch.dispose();
    }
}
