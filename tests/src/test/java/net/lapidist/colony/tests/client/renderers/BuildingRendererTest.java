package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class BuildingRendererTest {

    @Test
    public void rendersBuildingTexture() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);

        CameraProvider camera = mock(CameraProvider.class);
        Viewport viewport = mock(Viewport.class);
        when(camera.getViewport()).thenReturn(viewport);
        doAnswer(inv -> {
            Vector3 v = inv.getArgument(0);
            v.set(0f, 0f, 0f);
            return v;
        }).when(viewport).project(any(Vector3.class));

        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, new DefaultAssetResolver());

        Array<RenderBuilding> buildings = new Array<>();
        RenderBuilding building = RenderBuilding.builder()
                .x(1)
                .y(2)
                .worldX(1 * GameConstants.TILE_SIZE)
                .worldY(2 * GameConstants.TILE_SIZE)
                .buildingType("HOUSE")
                .build();
        buildings.add(building);

        MapRenderData map = new SimpleMapRenderData(new Array<RenderTile>(), buildings,
                new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT]);

        renderer.render(map);

        org.mockito.ArgumentCaptor<Float> xCap = org.mockito.ArgumentCaptor.forClass(Float.class);
        org.mockito.ArgumentCaptor<Float> yCap = org.mockito.ArgumentCaptor.forClass(Float.class);
        verify(batch).draw(eq(region), xCap.capture(), yCap.capture());
        org.junit.Assert.assertEquals(building.getWorldX(), xCap.getValue(), 0f);
        org.junit.Assert.assertEquals(building.getWorldY(), yCap.getValue(), 0f);
    }
}
