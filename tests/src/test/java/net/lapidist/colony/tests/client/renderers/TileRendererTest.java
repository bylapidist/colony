package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.renderers.AssetResolver;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.base.BaseDefinitionsMod;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.TileRotationUtil;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.settings.GraphicsSettings;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Texture;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
@SuppressWarnings("checkstyle:magicnumber")
public class TileRendererTest {

    private static final int CUSTOM_POWER = 11;
    private static final float CUSTOM_STRENGTH = 0.5f;
    private static final float EPSILON = 0.001f;
    private static final float RIGHT_ANGLE = 90f;
    private static final float INDEX_SCALE = 4f;

    @Test
    public void drawsTileAndOverlay() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        GraphicsSettings graphics = new GraphicsSettings();
        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null, graphics);
        reset(loader);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(true)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile);

        RenderTile tile2 = RenderTile.builder()
                .x(1)
                .y(1)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile2);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile;
        grid[1][1] = tile2;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        verify(batch).draw(any(TextureRegion.class), anyFloat(), anyFloat());
        verify(batch, times(tiles.size))
                .draw(
                        any(TextureRegion.class),
                        anyFloat(), anyFloat(),
                        anyFloat(), anyFloat(),
                        anyFloat(), anyFloat(),
                        anyFloat(), anyFloat(),
                        anyFloat());
    }

    @Test
    public void cachesTextureRegions() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        GraphicsSettings graphics = new GraphicsSettings();
        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null, graphics);
        reset(loader);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(true)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);
        renderer.render(map);

        verifyNoInteractions(loader);
    }

    @Test
    public void drawsLabelWhenAssetMissing() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        AssetResolver resolver = mock(AssetResolver.class);
        when(resolver.tileAsset(anyString())).thenReturn("dirt0");
        when(resolver.hasTileAsset(anyString())).thenReturn(false);

        GraphicsSettings graphics = new GraphicsSettings();
        TileRenderer renderer = new TileRenderer(batch, loader, camera, resolver, null, graphics);

        java.lang.reflect.Field fontField = TileRenderer.class.getDeclaredField("font");
        fontField.setAccessible(true);
        BitmapFont font = spy(new BitmapFont());
        fontField.set(renderer, font);
        java.lang.reflect.Field layoutField = TileRenderer.class.getDeclaredField("layout");
        layoutField.setAccessible(true);
        GlyphLayout layout = spy(new GlyphLayout());
        layoutField.set(renderer, layout);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        verify(font).draw(eq(batch), eq(layout), anyFloat(), anyFloat());
    }

    @Test
    public void rotatesTilesIndividually() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        GraphicsSettings graphics = new GraphicsSettings();
        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null, graphics);
        reset(loader);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile1 = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile1);

        RenderTile tile2 = RenderTile.builder()
                .x(1)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile2);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile1;
        grid[1][0] = tile2;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        ArgumentCaptor<Float> rotCaptor = ArgumentCaptor.forClass(Float.class);
        verify(batch, times(2))
                .draw(
                        eq(region),
                        anyFloat(), anyFloat(),
                        anyFloat(), anyFloat(),
                        anyFloat(), anyFloat(),
                        anyFloat(), anyFloat(),
                        rotCaptor.capture()
                );

        List<Float> values = rotCaptor.getAllValues();
        assertEquals(2, values.size());
        assertNotEquals(values.get(0), values.get(1));
    }

    @Test
    public void doesNotBindTexturesWhenDisabled() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ShaderProgram shader = mock(ShaderProgram.class);
        when(batch.getShader()).thenReturn(shader);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        TextureRegion normal = mock(TextureRegion.class);
        TextureRegion spec = mock(TextureRegion.class);
        Texture normalTex = mock(Texture.class);
        Texture specTex = mock(Texture.class);
        when(normal.getTexture()).thenReturn(normalTex);
        when(spec.getTexture()).thenReturn(specTex);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);
        when(loader.findNormalRegion(anyString())).thenReturn(normal);
        when(loader.findSpecularRegion(anyString())).thenReturn(spec);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        GraphicsSettings graphics = new GraphicsSettings();
        graphics.setNormalMapsEnabled(false);
        graphics.setSpecularMapsEnabled(false);

        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null, graphics);
        reset(loader);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        verify(normalTex, never()).bind(anyInt());
        verify(specTex, never()).bind(anyInt());
    }

    @Test
    public void setsSpecularPowerUniform() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ShaderProgram shader = mock(ShaderProgram.class);
        when(batch.getShader()).thenReturn(shader);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);
        when(loader.getSpecularPower(anyString())).thenReturn(CUSTOM_POWER);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        GraphicsSettings graphics = new GraphicsSettings();
        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null, graphics);
        reset(loader);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        verify(shader).setUniformf("u_specularPower", (float) CUSTOM_POWER);
    }

    @Test
    public void setsNormalStrengthUniform() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ShaderProgram shader = mock(ShaderProgram.class);
        when(batch.getShader()).thenReturn(shader);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        GraphicsSettings graphics = new GraphicsSettings();
        graphics.setNormalMapStrength(CUSTOM_STRENGTH);
        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null, graphics);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        verify(shader).setUniformf("u_normalStrength", CUSTOM_STRENGTH);
    }

    @Test
    public void setsTileRotationUniform() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ShaderProgram shader = mock(ShaderProgram.class);
        when(batch.getShader()).thenReturn(shader);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        GraphicsSettings graphics = new GraphicsSettings();
        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null, graphics);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile1 = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile1);

        RenderTile tile2 = RenderTile.builder()
                .x(1)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(0)
                .stone(0)
                .food(0)
                .build();
        tiles.add(tile2);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile1;
        grid[1][0] = tile2;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        ArgumentCaptor<Float> captor = ArgumentCaptor.forClass(Float.class);
        verify(batch, times(2)).setColor(eq(1f), eq(1f), eq(1f), captor.capture());
        java.util.List<Float> vals = captor.getAllValues();
        int index1 = (int) (TileRotationUtil.rotationFor(0, 0) / RIGHT_ANGLE);
        int index2 = (int) (TileRotationUtil.rotationFor(1, 0) / RIGHT_ANGLE);
        assertEquals(index1 / INDEX_SCALE, vals.get(0), EPSILON);
        assertEquals(index2 / INDEX_SCALE, vals.get(1), EPSILON);
    }
}
