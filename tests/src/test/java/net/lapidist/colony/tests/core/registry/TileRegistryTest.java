package net.lapidist.colony.tests.core.registry;

import net.lapidist.colony.registry.TileDefinition;
import net.lapidist.colony.registry.TileRegistry;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileRegistryTest {
    @Test
    public void getIsCaseInsensitive() {
        TileRegistry registry = new TileRegistry();
        TileDefinition def = new TileDefinition("grass", "Grass", "grass0");
        registry.register(def);

        assertSame(def, registry.get("GRASS"));
        assertSame(def, registry.get("grass"));
        assertSame(def, registry.get("GrAsS"));
    }

    @Test
    public void registerIgnoresNulls() {
        TileRegistry registry = new TileRegistry();
        registry.register(null);
        registry.register(new TileDefinition(null, null, null));
        assertTrue(registry.all().isEmpty());
    }

    @Test
    public void allReturnsRegisteredDefinitions() {
        TileRegistry registry = new TileRegistry();
        TileDefinition a = new TileDefinition("grass", "Grass", "grass0");
        TileDefinition b = new TileDefinition("dirt", "Dirt", "dirt0");
        registry.register(a);
        registry.register(b);

        assertEquals(2, registry.all().size());
        assertTrue(registry.all().contains(a));
        assertTrue(registry.all().contains(b));
    }
}
