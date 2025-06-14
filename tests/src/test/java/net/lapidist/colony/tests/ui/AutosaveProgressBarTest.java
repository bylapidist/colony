package net.lapidist.colony.tests.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.lapidist.colony.client.ui.AutosaveProgressBar;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.server.events.AutosaveStartEvent;
import net.lapidist.colony.tests.GdxTestRunner;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.file.Path;

import static org.junit.Assert.*;

/** Tests for {@link AutosaveProgressBar}. */
@RunWith(GdxTestRunner.class)
public class AutosaveProgressBarTest {

    private static final float ADVANCE = 2.5f;

    @Test
    public void showsProgressDuringAutosave() {
        EventSystem system = new EventSystem();
        Events.init(system);
        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));
        AutosaveProgressBar bar = new AutosaveProgressBar(skin);

        Events.dispatch(new AutosaveStartEvent(Path.of("tmp")));
        Events.update();
        assertTrue(bar.isVisible());
        assertEquals(0f, bar.getValue(), 0f);

        bar.act(1f);
        assertTrue(bar.getValue() > 0f);

        Events.dispatch(new AutosaveEvent(Path.of("tmp"), 1L));
        Events.update();
        assertEquals(1f, bar.getValue(), 0f);

        bar.act(ADVANCE);
        assertFalse(bar.isVisible());
    }
}
