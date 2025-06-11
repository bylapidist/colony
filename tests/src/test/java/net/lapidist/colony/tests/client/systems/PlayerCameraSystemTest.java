package net.lapidist.colony.tests.client.systems;

import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.client.systems.PlayerCameraSystem;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertSame;

@RunWith(GdxTestRunner.class)
public class PlayerCameraSystemTest {

    @Test
    public void reuseVectorConversions() {
        PlayerCameraSystem cameraSystem = new PlayerCameraSystem();
        Vector2 out = new Vector2();

        Vector2 result = cameraSystem.cameraCoordsFromWorldCoords(0f, 0f, out);
        assertSame(out, result);

        result = cameraSystem.worldCoordsFromCameraCoords(0f, 0f, out);
        assertSame(out, result);

        result = cameraSystem.screenCoordsToWorldCoords(0f, 0f, out);
        assertSame(out, result);

        result = cameraSystem.tileCoordsToWorldCoords(0, 0, out);
        assertSame(out, result);

        result = cameraSystem.tileCoordsToWorldCoords(new Vector2(), out);
        assertSame(out, result);

        result = cameraSystem.worldCoordsToTileCoords(0, 0, out);
        assertSame(out, result);

        result = cameraSystem.worldCoordsToTileCoords(new Vector2(), out);
        assertSame(out, result);
    }
}
