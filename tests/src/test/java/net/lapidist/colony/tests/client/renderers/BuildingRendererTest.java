package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.renderers.AssetResolver;
import net.lapidist.colony.base.BaseDefinitionsMod;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.RenderBuilding;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.RenderTile;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class BuildingRendererTest {

    private static final int VIEW_SIZE = 32;

    @Test
    public void rendersBuildingTexture() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);

        new BaseDefinitionsMod().init();

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
        RenderBuilding building = RenderBuilding.builder().x(0).y(0).buildingType("house").build();
        buildings.add(building);

        MapRenderData map = new SimpleMapRenderData(new Array<RenderTile>(), buildings,
                new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT]);

        renderer.render(map);

        verify(batch).draw(eq(region), anyFloat(), anyFloat());
    }

    @Test
    public void cachesTextureRegions() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);

        new BaseDefinitionsMod().init();

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
        RenderBuilding building = RenderBuilding.builder().x(0).y(0).buildingType("house").build();
        buildings.add(building);

        MapRenderData map = new SimpleMapRenderData(new Array<RenderTile>(), buildings,
                new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT]);

        renderer.render(map);
        renderer.render(map);

        verifyNoInteractions(loader);
    }

    @Test
    public void drawsLabelWhenAssetMissing() throws Exception {
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

        AssetResolver resolver = mock(AssetResolver.class);
        when(resolver.buildingAsset(anyString())).thenReturn("house0");
        when(resolver.hasBuildingAsset(anyString())).thenReturn(false);

        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, resolver);

        java.lang.reflect.Field fontField = BuildingRenderer.class.getDeclaredField("font");
        fontField.setAccessible(true);
        BitmapFont font = spy(new BitmapFont());
        fontField.set(renderer, font);
        java.lang.reflect.Field layoutField = BuildingRenderer.class.getDeclaredField("layout");
        layoutField.setAccessible(true);
        GlyphLayout layout = spy(new GlyphLayout());
        layoutField.set(renderer, layout);

        Array<RenderBuilding> buildings = new Array<>();
        RenderBuilding building = RenderBuilding.builder().x(0).y(0).buildingType("house").build();
        buildings.add(building);

        MapRenderData map = new SimpleMapRenderData(new Array<RenderTile>(), buildings,
                new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT]);

        renderer.render(map);

        verify(font).draw(eq(batch), eq(layout), anyFloat(), anyFloat());
    }
}
