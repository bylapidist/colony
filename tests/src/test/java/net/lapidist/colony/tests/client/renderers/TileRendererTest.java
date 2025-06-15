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
import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class TileRendererTest {

    @Test
    public void drawsTileAndOverlay() {
        SpriteBatch batch = mock(SpriteBatch.class);
        ResourceLoader loader = mock(ResourceLoader.class);
        TextureRegion region = mock(TextureRegion.class);
        TextureRegion overlay = mock(TextureRegion.class);
        when(loader.findRegion(anyString())).thenReturn(region);
        when(loader.findRegion(eq("hoveredTile0"))).thenReturn(overlay);
        when(loader.findNormalRegion(anyString())).thenReturn(null);
        when(loader.findSpecularRegion(anyString())).thenReturn(null);
        when(loader.findNormalRegion(anyString())).thenReturn(null);
        when(loader.findSpecularRegion(anyString())).thenReturn(null);
        when(loader.findNormalRegion(anyString())).thenReturn(null);
        when(loader.findSpecularRegion(anyString())).thenReturn(null);
        when(loader.findNormalRegion(anyString())).thenReturn(null);
        when(loader.findSpecularRegion(anyString())).thenReturn(null);

        new BaseDefinitionsMod().init();

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null);
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

        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null);
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

        TileRenderer renderer = new TileRenderer(batch, loader, camera, resolver, null);

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

        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver(), null);
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
}
