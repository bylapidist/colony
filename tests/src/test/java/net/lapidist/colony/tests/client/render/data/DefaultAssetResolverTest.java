package net.lapidist.colony.tests.client.render.data;

import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import net.lapidist.colony.base.BaseDefinitionsMod;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultAssetResolverTest {
    @Test
    public void returnsSpriteReferences() {
        new BaseDefinitionsMod().init();
        DefaultAssetResolver resolver = new DefaultAssetResolver();
        assertEquals("grass0", resolver.tileAsset("GRASS"));
        assertEquals("grass0", resolver.tileAsset("grass"));
        assertEquals("dirt0", resolver.tileAsset("DIRT"));
        assertEquals("dirt0", resolver.tileAsset("EMPTY"));
        assertEquals("dirt0", resolver.tileAsset(null));
        assertEquals("dirt0", resolver.tileAsset("invalid"));
        assertTrue(resolver.hasTileAsset("GRASS"));
        assertFalse(resolver.hasTileAsset("invalid"));
        assertEquals("house0", resolver.buildingAsset("house"));
        assertEquals("house0", resolver.buildingAsset("house"));
        assertEquals("house0", resolver.buildingAsset("market"));
        assertEquals("house0", resolver.buildingAsset("factory"));
        assertEquals("house0", resolver.buildingAsset("farm"));
        assertEquals("house0", resolver.buildingAsset(null));
        assertEquals("house0", resolver.buildingAsset("invalid"));
        assertTrue(resolver.hasBuildingAsset("house"));
        assertTrue(resolver.hasBuildingAsset("farm"));
        assertFalse(resolver.hasBuildingAsset("invalid"));
    }
}
