package net.lapidist.colony.tests.core.registry;

import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.BuildingRegistry;
import net.lapidist.colony.registry.Registries;
import net.lapidist.colony.registry.TileDefinition;
import net.lapidist.colony.registry.TileRegistry;
import org.junit.Test;

import static org.junit.Assert.*;

/** Tests for simple registry implementations. */
public class RegistryTest {

    @Test
    public void registersAndRetrievesTile() {
        TileRegistry registry = new TileRegistry();
        TileDefinition def = new TileDefinition("grass", "Grass", "grass0");
        registry.register(def);
        assertEquals(def, registry.get("GRASS"));
    }

    @Test
    public void registersAndRetrievesBuilding() {
        BuildingRegistry registry = new BuildingRegistry();
        BuildingDefinition def = new BuildingDefinition("house", "House", "house0");
        registry.register(def);
        assertEquals(def, registry.get("house"));
    }

    @Test
    public void globalAccessorsReturnSingletons() {
        assertSame(Registries.tiles(), Registries.tiles());
        assertSame(Registries.buildings(), Registries.buildings());
        assertSame(Registries.items(), Registries.items());
    }
}
