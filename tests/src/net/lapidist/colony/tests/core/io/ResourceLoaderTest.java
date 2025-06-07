package net.lapidist.colony.tests.core.io;

import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class ResourceLoaderTest {

    @Test
    public final void testLoadsResources() throws IOException {
        ResourceLoader resourceLoader = new ResourceLoader();

        resourceLoader.load(
                FileLocation.INTERNAL,
                "resources.conf"
        );
        assertTrue(resourceLoader.isLoaded());
    }
}
