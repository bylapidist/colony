package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
    private final PlayerRenderer playerRenderer;
    private final ShaderProgram shaderProgram;
    private final MapTileCache tileCache = new MapTileCache();
    private final AssetResolver resolver = new DefaultAssetResolver();
    private final boolean cacheEnabled;

    @SuppressWarnings("checkstyle:ParameterNumber")
    public SpriteBatchMapRenderer(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final TileRenderer tileRendererToSet,
            final BuildingRenderer buildingRendererToSet,
            final ResourceRenderer resourceRendererToSet,
            final PlayerRenderer playerRendererToSet,
            final boolean cacheEnabledToSet,
            final ShaderProgram shaderProgramToUse
    ) {
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.tileRenderer = tileRendererToSet;
        this.buildingRenderer = buildingRendererToSet;
        this.resourceRenderer = resourceRendererToSet;
        this.playerRenderer = playerRendererToSet;
        this.cacheEnabled = cacheEnabledToSet;
        this.shaderProgram = shaderProgramToUse;
    }

    /** Invalidate cache segments for the given tile indices. */
    public void invalidateTiles(final com.badlogic.gdx.utils.IntArray indices) {
        if (cacheEnabled) {
            tileCache.invalidateTiles(indices);
        }
    }

    @Override
    public void render(final MapRenderData map, final CameraProvider camera) {
        spriteBatch.setProjectionMatrix(camera.getCamera().combined);
        if (cacheEnabled) {
            tileCache.ensureCache(resourceLoader, map, resolver, camera);
        }
        ShaderProgram oldShader = null;
        if (shaderProgram != null) {
            oldShader = spriteBatch.getShader();
            spriteBatch.setShader(shaderProgram);
        }
        spriteBatch.begin();

        if (cacheEnabled) {
            tileCache.draw(spriteBatch, camera);
            tileRenderer.setOverlayOnly(true);
        } else {
            tileRenderer.setOverlayOnly(false);
        }

        tileRenderer.render(map);
        buildingRenderer.render(map);
        resourceRenderer.render(map);
        playerRenderer.render(map);

        spriteBatch.end();
        if (shaderProgram != null) {
            spriteBatch.setShader(oldShader);
        }
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        resourceRenderer.dispose();
        playerRenderer.dispose();
        spriteBatch.dispose();
        tileCache.dispose();
        if (shaderProgram != null) {
            shaderProgram.dispose();
        }
    }
}
