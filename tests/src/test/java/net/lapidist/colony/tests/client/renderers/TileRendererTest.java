package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.client.renderers.TileRenderer;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
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

        CameraProvider camera = mock(CameraProvider.class);
        com.badlogic.gdx.graphics.OrthographicCamera cam = new com.badlogic.gdx.graphics.OrthographicCamera();
        com.badlogic.gdx.utils.viewport.ExtendViewport viewport =
                new com.badlogic.gdx.utils.viewport.ExtendViewport(1f, 1f, cam);
        cam.update();
        when(camera.getViewport()).thenReturn(viewport);
        when(camera.getCamera()).thenReturn(cam);

        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver());

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

        RenderTile[][] grid = new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT];
        grid[0][0] = tile;
        grid[1][1] = tile2;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        int expectedDraws = tiles.size + 1;
        verify(batch, times(expectedDraws)).draw(any(TextureRegion.class), anyFloat(), anyFloat());
    }

    @Test
    public void cachesTextureRegions() {
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

        TileRenderer renderer = new TileRenderer(batch, loader, camera, new DefaultAssetResolver());

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

        RenderTile[][] grid = new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);
        renderer.render(map);

        verify(loader, times(1)).findRegion("grass0");
        verify(loader, times(1)).findRegion("hoveredTile0");
    }
}
