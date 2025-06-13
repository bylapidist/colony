package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.Gdx;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.mod.PrototypeManager;
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

    private static final int VIEW_SIZE = 32;

    @Test
    public void rendersBuildingTexture() {
        PrototypeManager.load(Gdx.files.internal("sample-mod.json"));
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);

        CameraProvider camera = mock(CameraProvider.class);
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        viewport.update(VIEW_SIZE, VIEW_SIZE, true);
        cam.update();
        when(camera.getCamera()).thenReturn(cam);
        when(camera.getViewport()).thenReturn(viewport);

        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, new DefaultAssetResolver());
        reset(loader);

        Array<RenderBuilding> buildings = new Array<>();
        RenderBuilding building = RenderBuilding.builder().x(0).y(0).buildingType("HOUSE").build();
        buildings.add(building);

        MapRenderData map = new SimpleMapRenderData(new Array<RenderTile>(), buildings,
                new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT]);

        renderer.render(map);

        verify(batch).draw(eq(region), anyFloat(), anyFloat());
    }

    @Test
    public void cachesTextureRegions() {
        PrototypeManager.load(Gdx.files.internal("sample-mod.json"));
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);

        CameraProvider camera = mock(CameraProvider.class);
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        viewport.update(VIEW_SIZE, VIEW_SIZE, true);
        cam.update();
        when(camera.getCamera()).thenReturn(cam);
        when(camera.getViewport()).thenReturn(viewport);

        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, new DefaultAssetResolver());
        reset(loader);

        Array<RenderBuilding> buildings = new Array<>();
        RenderBuilding building = RenderBuilding.builder().x(0).y(0).buildingType("HOUSE").build();
        buildings.add(building);

        MapRenderData map = new SimpleMapRenderData(new Array<RenderTile>(), buildings,
                new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT]);

        renderer.render(map);
        renderer.render(map);

        verifyNoInteractions(loader);
    }
}
