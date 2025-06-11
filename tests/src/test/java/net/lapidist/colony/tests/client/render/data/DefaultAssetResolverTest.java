package net.lapidist.colony.tests.client.render.data;

import net.lapidist.colony.client.renderers.DefaultAssetResolver;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultAssetResolverTest {
    @Test
    public void returnsSpriteReferences() {
        DefaultAssetResolver resolver = new DefaultAssetResolver();
        assertEquals("grass0", resolver.tileAsset("GRASS"));
        assertEquals("grass0", resolver.tileAsset("grass"));
        assertEquals("house0", resolver.buildingAsset("HOUSE"));
        assertEquals("house0", resolver.buildingAsset("house"));
    }
}
