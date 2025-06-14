package net.lapidist.colony.tests.core.registry;

import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.BuildingRegistry;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingRegistryTest {
    @Test
    public void getIsCaseInsensitive() {
        BuildingRegistry registry = new BuildingRegistry();
        BuildingDefinition def = new BuildingDefinition("house", "House", "house0");
        registry.register(def);

        assertSame(def, registry.get("house"));
        assertSame(def, registry.get("HOUSE"));
        assertSame(def, registry.get("HoUsE"));
    }

    @Test
    public void registerIgnoresNulls() {
        BuildingRegistry registry = new BuildingRegistry();
        registry.register(null);
        registry.register(new BuildingDefinition(null, null, null));
        assertTrue(registry.all().isEmpty());
    }

    @Test
    public void allReturnsRegisteredDefinitions() {
        BuildingRegistry registry = new BuildingRegistry();
        BuildingDefinition house = new BuildingDefinition("house", "House", "house0");
        BuildingDefinition farm = new BuildingDefinition("farm", "Farm", "farm0");
        registry.register(house);
        registry.register(farm);

        assertEquals(2, registry.all().size());
        assertTrue(registry.all().contains(house));
        assertTrue(registry.all().contains(farm));
    }
}
