package net.lapidist.colony.tests.core.registry;

import net.lapidist.colony.registry.ResourceDefinition;
import net.lapidist.colony.registry.ResourceRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/** Unit tests for {@link ResourceRegistry}. */
public class ResourceRegistryTest {
    private ResourceRegistry registry;

    @Before
    public void setUp() {
        registry = new ResourceRegistry();
    }

    @Test
    public void registerStoresDefinitionById() {
        ResourceDefinition def = new ResourceDefinition("wood", "Wood", "wood0");
        registry.register(def);

        assertSame(def, registry.get("wood"));
        // Case insensitive
        assertSame(def, registry.get("WOOD"));
    }

    @Test
    public void ignoresNullDefinitions() {
        registry.register(null);
        registry.register(new ResourceDefinition());
        assertNull(registry.get(null));
        assertTrue(registry.all().isEmpty());
    }

    @Test
    public void allReturnsAllRegisteredDefinitions() {
        registry.register(new ResourceDefinition("wood", "Wood", "wood0"));
        registry.register(new ResourceDefinition("stone", "Stone", "stone0"));

        assertEquals(2, registry.all().size());
    }
}
