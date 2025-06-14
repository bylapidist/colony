package net.lapidist.colony.tests.client.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.mockito.ArgumentCaptor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.lapidist.colony.client.renderers.ResourceRenderer;
import net.lapidist.colony.client.systems.CameraProvider;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.systems.MapRenderDataSystem;
import net.lapidist.colony.components.state.MapState;
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
    private static final int VIEW_SIZE = 32;

    @Test
    public void rendersResourceTextWithoutErrors() {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        viewport.update(VIEW_SIZE, VIEW_SIZE, true);
        cam.update();
        when(camera.getCamera()).thenReturn(cam);
        when(camera.getViewport()).thenReturn(viewport);

        MapRenderDataSystem dataSystem = new MapRenderDataSystem();
        ResourceRenderer renderer = new ResourceRenderer(batch, camera, dataSystem);

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

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);
        renderer.dispose();
    }

    @Test
    public void drawsResourceTextForSelectedTile() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        viewport.update(VIEW_SIZE, VIEW_SIZE, true);
        cam.update();
        when(camera.getCamera()).thenReturn(cam);
        when(camera.getViewport()).thenReturn(viewport);

        MapRenderDataSystem dataSystem = new MapRenderDataSystem();
        dataSystem.getSelectedTileIndices().add(0);
        ResourceRenderer renderer = new ResourceRenderer(batch, camera, dataSystem);

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
                .tileType("GRASS")
                .selected(true)
                .wood(WOOD)
                .stone(STONE)
                .food(FOOD)
                .build();
        tiles.add(tile);

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tile;
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);

        ArgumentCaptor<CharSequence> captor = ArgumentCaptor.forClass(CharSequence.class);
        verify(layout).setText(eq(font), captor.capture());
        assertEquals(WOOD + "/" + STONE + "/" + FOOD, captor.getValue().toString());
        verify(font).draw(eq(batch), eq(layout), anyFloat(), anyFloat());
        renderer.dispose();
    }

    @Test
    public void skipsUnselectedTiles() throws Exception {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        viewport.update(VIEW_SIZE, VIEW_SIZE, true);
        cam.update();
        when(camera.getCamera()).thenReturn(cam);
        when(camera.getViewport()).thenReturn(viewport);

        MapRenderDataSystem dataSystem = new MapRenderDataSystem();
        ResourceRenderer renderer = new ResourceRenderer(batch, camera, dataSystem);

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

        RenderTile[][] grid2 = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid2[0][0] = tile2;
        MapRenderData map2 = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid2);

        renderer.render(map2);

        verify(font, never()).draw(any(SpriteBatch.class), any(GlyphLayout.class), anyFloat(), anyFloat());
        renderer.dispose();
    }

    @Test
    public void doesNotIterateAllTiles() {
        SpriteBatch batch = mock(SpriteBatch.class);
        CameraProvider camera = mock(CameraProvider.class);
        OrthographicCamera cam = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(VIEW_SIZE, VIEW_SIZE, cam);
        viewport.update(VIEW_SIZE, VIEW_SIZE, true);
        cam.update();
        when(camera.getCamera()).thenReturn(cam);
        when(camera.getViewport()).thenReturn(viewport);

        MapRenderDataSystem dataSystem = new MapRenderDataSystem();
        dataSystem.getSelectedTileIndices().add(0);
        ResourceRenderer renderer = new ResourceRenderer(batch, camera, dataSystem);

        class GuardArray<T> extends Array<T> {
            @Override
            public T get(final int index) {
                if (index > 0) {
                    throw new AssertionError("accessed " + index);
                }
                return super.get(index);
            }
        }

        GuardArray<RenderTile> tiles = new GuardArray<>();
        final int tileCount = 3;
        for (int i = 0; i < tileCount; i++) {
            tiles.add(RenderTile.builder()
                    .x(i)
                    .y(0)
                    .tileType("GRASS")
                    .selected(i == 0)
                    .wood(WOOD)
                    .stone(STONE)
                    .food(FOOD)
                    .build());
        }

        RenderTile[][] grid = new RenderTile[MapState.DEFAULT_WIDTH][MapState.DEFAULT_HEIGHT];
        grid[0][0] = tiles.first();
        MapRenderData map = new SimpleMapRenderData(tiles, new Array<RenderBuilding>(), grid);

        renderer.render(map);
        renderer.dispose();
    }
}
