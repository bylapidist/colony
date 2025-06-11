package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.renderers.ModelBatchMapRenderer;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class ModelBatchMapRendererTest {

    @Test
    public void rendersModelBatch() {
        ModelBatch batch = mock(ModelBatch.class);
        ModelInstance instance = mock(ModelInstance.class);
        CameraProvider cameraProvider = mock(CameraProvider.class);
        Camera camera = mock(Camera.class);
        when(cameraProvider.getCamera()).thenReturn(camera);

        Array<RenderTile> tiles = new Array<>();
        tiles.add(RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build());

        MapRenderData map = new SimpleMapRenderData(
                tiles,
                new Array<RenderBuilding>(),
                new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT]
        );

        ModelBatchMapRenderer renderer = new ModelBatchMapRenderer(batch, instance);
        renderer.render(map, cameraProvider);
        renderer.dispose();

        verify(batch).begin(camera);
        verify(batch, atLeastOnce()).render(instance);
        verify(batch).end();
        verify(batch).dispose();
    }
}
