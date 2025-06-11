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
        when(viewport.project(any(Vector3.class))).thenReturn(new Vector3());

        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, new DefaultAssetResolver());

        Array<RenderBuilding> buildings = new Array<>();
        buildings.add(RenderBuilding.builder().x(0).y(0).buildingType("HOUSE").build());

        renderer.render(buildings);

        verify(batch).draw(eq(region), anyFloat(), anyFloat());
    }
}
