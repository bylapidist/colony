package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import box2dLight.RayHandler;
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
    private final MapEntityRenderers entityRenderers;
    private final ShaderProgram shader;
    private net.lapidist.colony.client.graphics.ShaderPlugin plugin;
    private final MapTileCache tileCache = new MapTileCache();
    private final AssetResolver resolver = new DefaultAssetResolver();
    private final boolean cacheEnabled;
    private RayHandler lights;

    public SpriteBatchMapRenderer(
            final SpriteBatch spriteBatchToSet,
            final ResourceLoader resourceLoaderToSet,
            final TileRenderer tileRendererToSet,
            final BuildingRenderer buildingRendererToSet,
            final MapEntityRenderers entityRenderersToSet,
            final boolean cacheEnabledToSet,
            final ShaderProgram shaderToSet
    ) {
        this.spriteBatch = spriteBatchToSet;
        this.resourceLoader = resourceLoaderToSet;
        this.tileRenderer = tileRendererToSet;
        this.buildingRenderer = buildingRendererToSet;
        this.entityRenderers = entityRenderersToSet;
        this.cacheEnabled = cacheEnabledToSet;
        this.shader = shaderToSet;
    }

    /** Assign optional shader plugin. */
    public void setPlugin(final net.lapidist.colony.client.graphics.ShaderPlugin pluginToSet) {
        this.plugin = pluginToSet;
    }

    /** Invalidate cache segments for the given tile indices. */
    public void invalidateTiles(final com.badlogic.gdx.utils.IntArray indices) {
        if (cacheEnabled) {
            tileCache.invalidateTiles(indices);
        }
    }

    /** Set optional lighting handler. */
    public void setLights(final RayHandler handler) {
        this.lights = handler;
    }

    @Override
    public void render(final MapRenderData map, final CameraProvider camera) {
        spriteBatch.setProjectionMatrix(camera.getCamera().combined);
        if (cacheEnabled) {
            tileCache.ensureCache(resourceLoader, map, resolver, camera);
        }
        if (shader != null) {
            spriteBatch.setShader(shader);
        }
        spriteBatch.begin();
        if (shader != null && plugin instanceof net.lapidist.colony.client.graphics.UniformUpdater updater) {
            updater.applyUniforms(shader);
        }

        if (cacheEnabled) {
            tileCache.draw(spriteBatch, camera);
        }
        tileRenderer.setOverlayOnly(false);

        tileRenderer.render(map);
        buildingRenderer.render(map);
        entityRenderers.resourceRenderer().render(map);
        entityRenderers.playerRenderer().render(map);
        entityRenderers.celestialRenderer().render(map);

        spriteBatch.end();
        if (shader != null) {
            spriteBatch.setShader(null);
        }
        if (lights != null) {
            com.badlogic.gdx.graphics.OrthographicCamera cam =
                    (com.badlogic.gdx.graphics.OrthographicCamera) camera.getCamera();
            com.badlogic.gdx.utils.viewport.Viewport vp = camera.getViewport();
            lights.setCombinedMatrix(
                    cam.combined,
                    cam.position.x,
                    cam.position.y,
                    vp.getWorldWidth() * cam.zoom,
                    vp.getWorldHeight() * cam.zoom
            );
            lights.render();
        }
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
        entityRenderers.resourceRenderer().dispose();
        spriteBatch.dispose();
        if (shader != null) {
            shader.dispose();
        }
        tileCache.dispose();
        if (plugin != null) {
            plugin.dispose();
        }
    }
}
