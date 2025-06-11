package net.lapidist.colony.tests.core.io;

import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.G3dResourceLoader;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.io.IOException;

@RunWith(GdxTestRunner.class)
public class G3dResourceLoaderTest {

    @Test
    public void loadsModelAndTextures() throws IOException {
        ResourceLoader loader = new G3dResourceLoader();
        loader.loadTextures(FileLocation.INTERNAL, "textures/textures.atlas");
        loader.loadModel(FileLocation.INTERNAL, "models/cube.g3dj");

        assertTrue(loader.isLoaded());
        assertNotNull(loader.getAtlas());
        assertNotNull(loader.findModel("models/cube.g3dj"));
    }

    @Test(expected = IOException.class)
    public void loadModelThrowsForMissingFile() throws IOException {
        ResourceLoader loader = new G3dResourceLoader();
        loader.loadModel(FileLocation.INTERNAL, "models/missing.g3dj");
    }
}
