package net.lapidist.colony.tests.core.registry;

import net.lapidist.colony.registry.ItemDefinition;
import net.lapidist.colony.registry.ItemRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/** Unit tests for {@link ItemRegistry}. */
public class ItemRegistryTest {
    private ItemRegistry registry;

    @Before
    public void setUp() {
        registry = new ItemRegistry();
    }

    @Test
    public void registerStoresDefinitionById() {
        ItemDefinition def = new ItemDefinition("stick", "Stick", "stick0");
        registry.register(def);

        assertSame(def, registry.get("stick"));
        // Case insensitive
        assertSame(def, registry.get("STICK"));
    }

    @Test
    public void ignoresNullDefinitions() {
        registry.register(null);
        registry.register(new ItemDefinition());
        assertNull(registry.get(null));
        assertTrue(registry.all().isEmpty());
    }

    @Test
    public void allReturnsAllRegisteredDefinitions() {
        registry.register(new ItemDefinition("stick", "Stick", "stick0"));
        registry.register(new ItemDefinition("stone", "Stone", "stone0"));

        assertEquals(2, registry.all().size());
    }
}
