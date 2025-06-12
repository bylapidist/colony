package net.lapidist.colony.tests.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import net.lapidist.colony.client.network.GameClient;
import net.lapidist.colony.client.screens.LoadingScreen;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class LoadingScreenTest {

    private static Table getRoot(final LoadingScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("root");
        f.setAccessible(true);
        return (Table) f.get(screen);
    }

    private static Stage getStage(final LoadingScreen screen) throws Exception {
        Field f = screen.getClass().getSuperclass().getDeclaredField("stage");
        f.setAccessible(true);
        return (Stage) f.get(screen);
    }

    @Test
    public void updatesProgressBarFromClient() throws Exception {
        GameClient client = mock(GameClient.class);
        final float progress = 0.5f;
        when(client.getLoadProgress()).thenReturn(progress);

        try (MockedConstruction<SpriteBatch> cons = mockConstruction(SpriteBatch.class,
                (mock, ctx) -> {
                    when(mock.getProjectionMatrix()).thenReturn(new Matrix4());
                })) {
            LoadingScreen screen = new LoadingScreen(client);
            Stage s = getStage(screen);
            ProgressBar bar = (ProgressBar) getRoot(screen).getChildren().get(1);
            s.act(0f);
            final float epsilon = 0.01f;
            assertEquals(progress, bar.getValue(), epsilon);
            screen.dispose();
        }

    }
}
