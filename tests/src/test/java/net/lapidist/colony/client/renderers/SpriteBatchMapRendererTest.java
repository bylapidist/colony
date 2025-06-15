package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.mockito.InOrder;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.junit.Test;
import org.junit.runner.RunWith;
import net.lapidist.colony.tests.GdxTestRunner;
import com.badlogic.gdx.utils.IntArray;
import net.lapidist.colony.client.render.MapRenderData;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class SpriteBatchMapRendererTest {

    @Test
    public void forwardsInvalidateCallsWhenCachingEnabled() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TileRenderer tileRenderer = mock(TileRenderer.class);
        BuildingRenderer buildingRenderer = mock(BuildingRenderer.class);
        ResourceRenderer resourceRenderer = mock(ResourceRenderer.class);
        PlayerRenderer playerRenderer = mock(PlayerRenderer.class);
        CelestialRenderer celestialRenderer = mock(CelestialRenderer.class);
        MapEntityRenderers renderers = new MapEntityRenderers(resourceRenderer, playerRenderer, celestialRenderer);

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, renderers, true, null);
        renderer.setPlugin(null);

        Field cacheField = SpriteBatchMapRenderer.class.getDeclaredField("tileCache");
        cacheField.setAccessible(true);
        MapTileCache cache = mock(MapTileCache.class);
        cacheField.set(renderer, cache);

        IntArray indices = new IntArray(new int[] {1});
        renderer.invalidateTiles(indices);

        verify(cache).invalidateTiles(indices);
    }

    @Test
    public void ignoreInvalidateWhenCachingDisabled() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TileRenderer tileRenderer = mock(TileRenderer.class);
        BuildingRenderer buildingRenderer = mock(BuildingRenderer.class);
        ResourceRenderer resourceRenderer = mock(ResourceRenderer.class);
        PlayerRenderer playerRenderer = mock(PlayerRenderer.class);
        CelestialRenderer celestialRenderer = mock(CelestialRenderer.class);
        MapEntityRenderers renderers = new MapEntityRenderers(resourceRenderer, playerRenderer, celestialRenderer);

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, renderers, false, null);
        renderer.setPlugin(null);

        Field cacheField = SpriteBatchMapRenderer.class.getDeclaredField("tileCache");
        cacheField.setAccessible(true);
        MapTileCache cache = mock(MapTileCache.class);
        cacheField.set(renderer, cache);

        renderer.invalidateTiles(new IntArray(new int[] {2}));

        verify(cache, never()).invalidateTiles(any(IntArray.class));
    }

    @Test
    public void appliesAndResetsShaderProgram() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TileRenderer tileRenderer = mock(TileRenderer.class);
        BuildingRenderer buildingRenderer = mock(BuildingRenderer.class);
        ResourceRenderer resourceRenderer = mock(ResourceRenderer.class);
        PlayerRenderer playerRenderer = mock(PlayerRenderer.class);
        CelestialRenderer celestialRenderer = mock(CelestialRenderer.class);
        ShaderProgram shader = mock(ShaderProgram.class);
        MapEntityRenderers renderers = new MapEntityRenderers(resourceRenderer, playerRenderer, celestialRenderer);

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, renderers, false, shader);
        renderer.setPlugin(null);

        MapRenderData map = mock(MapRenderData.class);
        CameraProvider camera = new CameraProvider() {
            private final OrthographicCamera cam = new OrthographicCamera();
            private final ExtendViewport vp = new ExtendViewport(1, 1, cam);
            {
                vp.update(1, 1, true);
                cam.update();
            }

            @Override
            public com.badlogic.gdx.graphics.Camera getCamera() {
                return cam;
            }

            @Override
            public com.badlogic.gdx.utils.viewport.Viewport getViewport() {
                return vp;
            }
        };

        InOrder order = inOrder(batch);
        renderer.render(map, camera);
        order.verify(batch).setProjectionMatrix(any());
        order.verify(batch).setShader(shader);
        order.verify(batch).begin();
        order.verify(batch).end();
        order.verify(batch).setShader(null);

        renderer.dispose();
        verify(shader).dispose();
    }

    @Test
    public void skipsShaderWhenNull() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TileRenderer tileRenderer = mock(TileRenderer.class);
        BuildingRenderer buildingRenderer = mock(BuildingRenderer.class);
        ResourceRenderer resourceRenderer = mock(ResourceRenderer.class);
        PlayerRenderer playerRenderer = mock(PlayerRenderer.class);
        CelestialRenderer celestialRenderer = mock(CelestialRenderer.class);
        MapEntityRenderers renderers = new MapEntityRenderers(resourceRenderer, playerRenderer, celestialRenderer);

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, renderers, false, null);
        renderer.setPlugin(null);

        MapRenderData map = mock(MapRenderData.class);
        CameraProvider camera = new CameraProvider() {
            private final OrthographicCamera cam = new OrthographicCamera();
            private final ExtendViewport vp = new ExtendViewport(1, 1, cam);
            {
                vp.update(1, 1, true);
                cam.update();
            }

            @Override
            public com.badlogic.gdx.graphics.Camera getCamera() {
                return cam;
            }

            @Override
            public com.badlogic.gdx.utils.viewport.Viewport getViewport() {
                return vp;
            }
        };

        renderer.render(map, camera);
        verify(batch, never()).setShader(any());

        renderer.dispose();
}

    @Test
    public void rendersLightsWhenAvailable() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TileRenderer tileRenderer = mock(TileRenderer.class);
        BuildingRenderer buildingRenderer = mock(BuildingRenderer.class);
        ResourceRenderer resourceRenderer = mock(ResourceRenderer.class);
        PlayerRenderer playerRenderer = mock(PlayerRenderer.class);
        CelestialRenderer celestialRenderer = mock(CelestialRenderer.class);
        MapEntityRenderers renderers = new MapEntityRenderers(resourceRenderer, playerRenderer, celestialRenderer);
        box2dLight.RayHandler lights = mock(box2dLight.RayHandler.class);

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, renderers, false, null);
        renderer.setPlugin(null);
        renderer.setLights(lights);

        MapRenderData map = mock(MapRenderData.class);
        CameraProvider camera = new CameraProvider() {
            private final OrthographicCamera cam = new OrthographicCamera();
            private final ExtendViewport vp = new ExtendViewport(1, 1, cam);
            {
                vp.update(1, 1, true);
                cam.update();
            }
            @Override
            public com.badlogic.gdx.graphics.Camera getCamera() {
                return cam;
            }
            @Override
            public com.badlogic.gdx.utils.viewport.Viewport getViewport() {
                return vp;
            }
        };

        renderer.render(map, camera);
        verify(lights).setCombinedMatrix(any(com.badlogic.gdx.math.Matrix4.class));
        verify(lights).render();

        renderer.dispose();
    }
}
