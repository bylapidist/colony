package net.lapidist.colony.tests.core.base;

import net.lapidist.colony.base.BaseDefinitionsMod;
import net.lapidist.colony.registry.Registries;
import org.junit.Test;

import static org.junit.Assert.*;

/** Unit tests for {@link BaseDefinitionsMod}. */
public class BaseDefinitionsModTest {
    @Test
    public void registersDefaultDefinitions() {
        BaseDefinitionsMod mod = new BaseDefinitionsMod();
        mod.init();

        assertNotNull(Registries.tiles().get("GRASS"));
        assertNotNull(Registries.tiles().get("DIRT"));
        assertNotNull(Registries.buildings().get("HOUSE"));
        assertNotNull(Registries.buildings().get("FARM"));
    }
}
