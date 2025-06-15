package net.lapidist.colony.client.renderers;

import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.client.render.MapRenderData;

import java.util.function.Consumer;

/**
 * Renderer that loads resources asynchronously and delegates rendering once ready.
 */
public final class LoadingSpriteMapRenderer implements MapRenderer, Disposable {

    private final World world;
    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final CameraProvider cameraSystem;
    private final boolean cacheEnabled;
    private final Consumer<Float> progressCallback;

    private MapRenderer delegate;

    public LoadingSpriteMapRenderer(
            final World worldContext,
            final SpriteBatch batchToUse,
            final ResourceLoader loaderToUse,
            final CameraProvider camera,
            final boolean cache,
            final Consumer<Float> callback
    ) {
        this.world = worldContext;
        this.spriteBatch = batchToUse;
        this.resourceLoader = loaderToUse;
        this.cameraSystem = camera;
        this.cacheEnabled = cache;
        this.progressCallback = callback;
        // ensure mapper initialization for render systems
        worldContext.getMapper(MapComponent.class);
    }

    @Override
    public void render(final MapRenderData map, final CameraProvider ignored) {
        if (delegate == null) {
            boolean done = resourceLoader.update();
            if (progressCallback != null) {
                progressCallback.accept(resourceLoader.getProgress());
            }
            if (!done) {
                return;
            }
            net.lapidist.colony.client.network.GameClient gc = null;
            MapRenderDataSystem ds = world.getSystem(MapRenderDataSystem.class);
            if (ds != null) {
                gc = ds.getClient();
            }
            TileRenderer tileRenderer = new TileRenderer(
                    spriteBatch,
                    resourceLoader,
                    cameraSystem,
                    new DefaultAssetResolver(),
                    gc
            );
            BuildingRenderer buildingRenderer = new BuildingRenderer(
                    spriteBatch,
                    resourceLoader,
                    cameraSystem,
                    new DefaultAssetResolver()
            );
            PlayerRenderer playerRenderer = new PlayerRenderer(
                    spriteBatch,
                    resourceLoader,
                    world
            );
            ResourceRenderer resourceRenderer = new ResourceRenderer(
                    spriteBatch,
                    cameraSystem,
                    world.getSystem(MapRenderDataSystem.class)
            );
            delegate = new SpriteBatchMapRenderer(
                    spriteBatch,
                    resourceLoader,
                    tileRenderer,
                    buildingRenderer,
                    resourceRenderer,
                    playerRenderer,
                    cacheEnabled,
                    null
            );
            if (progressCallback != null) {
                progressCallback.accept(1f);
            }
        }
        if (map != null) {
            delegate.render(map, cameraSystem);
        }
    }

    @Override
    public void dispose() {
        if (delegate instanceof Disposable disposable) {
            disposable.dispose();
        } else {
            resourceLoader.dispose();
            spriteBatch.dispose();
        }
    }
}
