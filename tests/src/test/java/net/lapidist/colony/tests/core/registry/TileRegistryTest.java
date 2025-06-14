package net.lapidist.colony.tests.core.registry;

import net.lapidist.colony.registry.TileDefinition;
import net.lapidist.colony.registry.TileRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/** Unit tests for {@link TileRegistry}. */
public class TileRegistryTest {
    private TileRegistry registry;

    @Before
    public void setUp() {
        registry = new TileRegistry();
    }

    @Test
    public void registerStoresDefinitionById() {
        TileDefinition def = new TileDefinition("grass", "Grass", "grass0");
        registry.register(def);

        assertSame(def, registry.get("grass"));
        // Case insensitive
        assertSame(def, registry.get("GRASS"));
    }

    @Test
    public void ignoresNullDefinitions() {
        registry.register(null);
        registry.register(new TileDefinition());
        assertNull(registry.get(null));
        assertTrue(registry.all().isEmpty());
    }

    @Test
    public void allReturnsAllRegisteredDefinitions() {
        registry.register(new TileDefinition("dirt", "Dirt", "dirt0"));
        registry.register(new TileDefinition("grass", "Grass", "grass0"));

        assertEquals(2, registry.all().size());
    }
}
