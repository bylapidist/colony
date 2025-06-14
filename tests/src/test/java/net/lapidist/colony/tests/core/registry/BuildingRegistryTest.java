package net.lapidist.colony.tests.core.registry;

import net.lapidist.colony.registry.BuildingDefinition;
import net.lapidist.colony.registry.BuildingRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/** Unit tests for {@link BuildingRegistry}. */
public class BuildingRegistryTest {
    private BuildingRegistry registry;

    @Before
    public void setUp() {
        registry = new BuildingRegistry();
    }

    @Test
    public void registerStoresDefinitionById() {
        BuildingDefinition def = new BuildingDefinition("house", "House", "house0");
        registry.register(def);

        assertSame(def, registry.get("house"));
        // Case insensitive
        assertSame(def, registry.get("HOUSE"));
    }

    @Test
    public void ignoresNullDefinitions() {
        registry.register(null);
        registry.register(new BuildingDefinition());
        assertNull(registry.get(null));
        assertTrue(registry.all().isEmpty());
    }

    @Test
    public void allReturnsAllRegisteredDefinitions() {
        registry.register(new BuildingDefinition("house", "House", "house0"));
        registry.register(new BuildingDefinition("market", "Market", "house0"));

        assertEquals(2, registry.all().size());
    }
}
