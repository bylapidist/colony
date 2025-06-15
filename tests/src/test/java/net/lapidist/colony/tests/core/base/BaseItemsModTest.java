package net.lapidist.colony.tests.core.base;

import net.lapidist.colony.base.BaseItemsMod;
import net.lapidist.colony.registry.Registries;
import org.junit.Test;

import static org.junit.Assert.*;

/** Unit tests for {@link BaseItemsMod}. */
public class BaseItemsModTest {
    @Test
    public void registersDefaultItems() {
        BaseItemsMod mod = new BaseItemsMod();
        mod.init();

        assertNotNull(Registries.items().get("stick"));
        assertNotNull(Registries.items().get("stone"));
    }
}
