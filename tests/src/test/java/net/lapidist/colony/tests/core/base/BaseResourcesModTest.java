package net.lapidist.colony.tests.core.base;

import net.lapidist.colony.base.BaseResourcesMod;
import net.lapidist.colony.registry.Registries;
import org.junit.Test;

import static org.junit.Assert.*;

/** Unit tests for {@link BaseResourcesMod}. */
public class BaseResourcesModTest {
    @Test
    public void registersDefaultResources() {
        BaseResourcesMod mod = new BaseResourcesMod();
        mod.init();

        assertNotNull(Registries.resources().get("WOOD"));
        assertNotNull(Registries.resources().get("STONE"));
        assertNotNull(Registries.resources().get("FOOD"));
    }
}
