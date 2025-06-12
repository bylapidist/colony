package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.mockito.ArgumentCaptor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.client.renderers.ResourceRenderer;
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
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ResourceRendererTest {

    private static final int WOOD = 1;
    private static final int STONE = 2;
    private static final int FOOD = 3;
    private static final float OFFSET_Y = 8f;

    @Test
    public void rendersResourceTextWithoutErrors() {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        Viewport viewport = mock(Viewport.class);
        when(camera.getViewport()).thenReturn(viewport);
        when(viewport.project(any(Vector3.class))).thenReturn(new Vector3());

        ResourceRenderer renderer = new ResourceRenderer(batch, camera);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build();
        tiles.add(tile);

        RenderTile[][] grid = new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);
        renderer.dispose();
    }

    @Test
    public void drawsResourceTextForSelectedTile() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        Viewport viewport = mock(Viewport.class);
        when(camera.getViewport()).thenReturn(viewport);
        when(viewport.project(any(Vector3.class))).thenReturn(new Vector3());

        ResourceRenderer renderer = new ResourceRenderer(batch, camera);

        BitmapFont font = spy(new BitmapFont());
        GlyphLayout layout = spy(new GlyphLayout());
        java.lang.reflect.Field fontField = ResourceRenderer.class.getDeclaredField("font");
        fontField.setAccessible(true);
        fontField.set(renderer, font);
        java.lang.reflect.Field layoutField = ResourceRenderer.class.getDeclaredField("layout");
        layoutField.setAccessible(true);
        layoutField.set(renderer, layout);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile = RenderTile.builder()
                .x(0)
                .y(0)
                .worldX(0f)
                .worldY(0f)
                .tileType("GRASS")
                .selected(true)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build();
        tiles.add(tile);

        RenderTile[][] grid = new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        ArgumentCaptor<CharSequence> captor = ArgumentCaptor.forClass(CharSequence.class);
        verify(layout).setText(eq(font), captor.capture());
        assertEquals(WOOD + "/" + STONE + "/" + FOOD, captor.getValue().toString());
        org.mockito.ArgumentCaptor<Float> xCap = org.mockito.ArgumentCaptor.forClass(Float.class);
        org.mockito.ArgumentCaptor<Float> yCap = org.mockito.ArgumentCaptor.forClass(Float.class);
        verify(font).draw(eq(batch), eq(layout), xCap.capture(), yCap.capture());
        org.junit.Assert.assertEquals(tile.getWorldX(), xCap.getValue(), 0f);
        org.junit.Assert.assertEquals(tile.getWorldY() + OFFSET_Y, yCap.getValue(), 0f);
        renderer.dispose();
    }

    @Test
    public void skipsUnselectedTiles() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        Viewport viewport = mock(Viewport.class);
        when(camera.getViewport()).thenReturn(viewport);
        when(viewport.project(any(Vector3.class))).thenReturn(new Vector3());

        ResourceRenderer renderer = new ResourceRenderer(batch, camera);

        BitmapFont font = spy(new BitmapFont());
        java.lang.reflect.Field fontField = ResourceRenderer.class.getDeclaredField("font");
        fontField.setAccessible(true);
        fontField.set(renderer, font);

        Array<RenderTile> tiles = new Array<>();
        RenderTile tile2 = RenderTile.builder()
                .x(0)
                .y(0)
                .tileType("GRASS")
                .selected(false)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build();
        tiles.add(tile2);

        RenderTile[][] grid2 = new RenderTile[GameConstants.MAP_WIDTH][GameConstants.MAP_HEIGHT];
        grid2[0][0] = tile2;
        MapRenderData map2 = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid2);

        renderer.render(map2);

        verify(font, never()).draw(any(SpriteBatch.class), any(GlyphLayout.class), anyFloat(), anyFloat());
        renderer.dispose();
    }
}
