package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import net.lapidist.colony.client.renderers.ResourceRenderer;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class ResourceRendererTest {

    private static final int WOOD = 1;
    private static final int STONE = 2;
    private static final int FOOD = 3;

    @Test
    public void rendersResourceTextWithoutErrors() {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        when(camera.getVisibleTileBounds()).thenReturn(new Rectangle(0, 0, 0, 0));

        ResourceRenderer renderer = new ResourceRenderer(batch, camera);

        Array<RenderTile> tiles = new Array<>();
        tiles.add(RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build());
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<>());

        renderer.render(map);
        renderer.dispose();
    }
}
