package net.lapidist.colony.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.lapidist.colony.client.core.io.ResourceLoader;
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

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, resourceRenderer, true);

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

        SpriteBatchMapRenderer renderer = new SpriteBatchMapRenderer(
                batch, loader, tileRenderer, buildingRenderer, resourceRenderer, false);

        Field cacheField = SpriteBatchMapRenderer.class.getDeclaredField("tileCache");
        cacheField.setAccessible(true);
        MapTileCache cache = mock(MapTileCache.class);
        cacheField.set(renderer, cache);

        renderer.invalidateTiles(new IntArray(new int[] {2}));

        verify(cache, never()).invalidateTiles(any(IntArray.class));
    }
}
