package net.lapidist.colony.tests.server;

import net.lapidist.colony.base.BaseItemsMod;
import net.lapidist.colony.server.services.InventoryService;
import net.lapidist.colony.components.state.MapState;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/** Unit tests for {@link InventoryService}. */
public class InventoryServiceTest {
    @Before
    public void setUp() {
        // ensure default items are registered
        new BaseItemsMod().init();
    }

    @Test
    public void addsItemsOnlyWhenRegistered() {
        MapState state = new MapState();
        AtomicReference<MapState> ref = new AtomicReference<>(state);
        ReentrantLock lock = new ReentrantLock();
        InventoryService inv = new InventoryService(ref::get, ref::set, lock);
        inv.addItem("stone", 2);
        final int unknownAmount = 5;
        inv.addItem("unknown", unknownAmount);

        assertEquals(2, inv.getAmount("stone"));
        assertEquals(0, inv.getAmount("unknown"));
    }
}
