package net.lapidist.colony.tests.server;

import net.lapidist.colony.base.BaseItemsMod;
import net.lapidist.colony.base.BaseResourcesMod;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.components.state.resources.ResourceData;
import net.lapidist.colony.components.state.map.TileData;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.server.commands.GatherCommand;
import net.lapidist.colony.server.commands.GatherCommandHandler;
import net.lapidist.colony.server.services.InventoryService;
import net.lapidist.colony.server.services.NetworkService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/** Tests for {@link GatherCommandHandler}. */
public class GatherCommandHandlerTest {
    @Before
    public void setup() {
        new BaseResourcesMod().init();
        new BaseItemsMod().init();
    }

    @Test
    public void ignoresUnknownResource() {
        MapState state = new MapState();
        AtomicReference<MapState> ref = new AtomicReference<>(state);
        ReentrantLock lock = new ReentrantLock();
        InventoryService inv = new InventoryService(ref::get, ref::set, lock);
        GatherCommandHandler handler = new GatherCommandHandler(
                ref::get,
                ref::set,
                mock(NetworkService.class),
                inv,
                lock
        );

        handler.handle(new GatherCommand(0, 0, "missing"));

        assertEquals(0, inv.getAmount("missing"));
    }

    @Test
    public void gathersAndAddsItem() {
        MapState state = new MapState();
        ResourceData res = new ResourceData(0, 1, 0);
        state.putTile(TileData.builder()
                .x(0).y(0).tileType("GRASS").passable(true)
                .resources(res)
                .build());
        AtomicReference<MapState> ref = new AtomicReference<>(state);
        ReentrantLock lock = new ReentrantLock();
        InventoryService inv = new InventoryService(ref::get, ref::set, lock);
        GatherCommandHandler handler = new GatherCommandHandler(
                ref::get,
                ref::set,
                mock(NetworkService.class),
                inv,
                lock
        );

        handler.handle(new GatherCommand(0, 0, Registries.resources().get("STONE").id()));

        assertEquals(1, ref.get().playerResources().stone());
        assertEquals(1, inv.getAmount("stone"));
    }
}
