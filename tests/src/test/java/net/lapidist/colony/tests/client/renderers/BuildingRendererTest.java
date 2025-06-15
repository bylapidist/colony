package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.lapidist.colony.client.renderers.BuildingRenderer;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.renderers.AssetResolver;
import net.lapidist.colony.base.BaseDefinitionsMod;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.tests.GdxTestRunner;
import net.lapidist.colony.settings.GraphicsSettings;
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

        GraphicsSettings gs = new GraphicsSettings();
        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, new DefaultAssetResolver(), gs);
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

        GraphicsSettings gs = new GraphicsSettings();
        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, new DefaultAssetResolver(), gs);
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

        GraphicsSettings gs = new GraphicsSettings();
        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, resolver, gs);

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

    @Test
    public void doesNotBindTexturesWhenDisabled() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ShaderProgram shader = mock(ShaderProgram.class);
        when(batch.getShader()).thenReturn(shader);

        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion normal = mock(TextureRegion.class);
        TextureRegion spec = mock(TextureRegion.class);
        com.badlogic.gdx.graphics.Texture texN = mock(com.badlogic.gdx.graphics.Texture.class);
        com.badlogic.gdx.graphics.Texture texS = mock(com.badlogic.gdx.graphics.Texture.class);
        when(normal.getTexture()).thenReturn(texN);
        when(spec.getTexture()).thenReturn(texS);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findNormalRegion(anyString())).thenReturn(normal);
        when(loader.findSpecularRegion(anyString())).thenReturn(spec);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        viewport.update(VIEW_SIZE, VIEW_SIZE, true);
        cam.update();
        when(camera.getCamera()).thenReturn(cam);
        when(camera.getViewport()).thenReturn(viewport);

        GraphicsSettings gs = new GraphicsSettings();
        gs.setNormalMapsEnabled(false);
        gs.setSpecularMapsEnabled(false);
        BuildingRenderer renderer = new BuildingRenderer(batch, loader, camera, new DefaultAssetResolver(), gs);
        reset(loader);

        Array<RenderBuilding> buildings = new Array<>();
        RenderBuilding building = RenderBuilding.builder().x(0).y(0).buildingType("house").build();
        buildings.add(building);

        MapRenderData map = new SimpleMapRenderData(new Array<RenderTile>(), buildings,
                new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT]);

        renderer.render(map);

        verify(texN, never()).bind(anyInt());
        verify(texS, never()).bind(anyInt());
        verify(shader, never()).setUniformi(eq("u_normal"), anyInt());
        verify(shader, never()).setUniformi(eq("u_specular"), anyInt());
    }
}
