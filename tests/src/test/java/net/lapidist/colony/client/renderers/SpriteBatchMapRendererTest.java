package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.MapRenderData;
import org.mockito.InOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import net.lapidist.colony.tests.GdxTestRunner;
import com.badlogic.gdx.utils.IntArray;

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

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, resourceRenderer, playerRenderer, true, null);

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

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, resourceRenderer, playerRenderer, false, null);

        Field cacheField = SpriteBatchMapRenderer.class.getDeclaredField("tileCache");
        cacheField.setAccessible(true);
        MapTileCache cache = mock(MapTileCache.class);
        cacheField.set(renderer, cache);

        renderer.invalidateTiles(new IntArray(new int[] {2}));

        verify(cache, never()).invalidateTiles(any(IntArray.class));
    }

    @Test
    public void usesShaderWhenProvided() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TileRenderer tileRenderer = mock(TileRenderer.class);
        BuildingRenderer buildingRenderer = mock(BuildingRenderer.class);
        ResourceRenderer resourceRenderer = mock(ResourceRenderer.class);
        PlayerRenderer playerRenderer = mock(PlayerRenderer.class);
        ShaderProgram program = mock(ShaderProgram.class);
        CameraProvider camera = mock(CameraProvider.class);
        Camera cam = new OrthographicCamera();
        when(camera.getCamera()).thenReturn(cam);

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, resourceRenderer, playerRenderer, false, program);

        renderer.render(mock(MapRenderData.class), camera);

        InOrder order = inOrder(batch);
        order.verify(batch).setProjectionMatrix(cam.combined);
        order.verify(batch).setShader(program);
        order.verify(batch).begin();
        order.verify(batch).end();
        order.verify(batch).setShader(null);
    }

    @Test
    public void disposesShader() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TileRenderer tileRenderer = mock(TileRenderer.class);
        BuildingRenderer buildingRenderer = mock(BuildingRenderer.class);
        ResourceRenderer resourceRenderer = mock(ResourceRenderer.class);
        PlayerRenderer playerRenderer = mock(PlayerRenderer.class);
        ShaderProgram program = mock(ShaderProgram.class);

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, resourceRenderer, playerRenderer, false, program);

        renderer.dispose();

        verify(program).dispose();
    }
}
