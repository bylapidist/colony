package net.lapidist.colony.tests.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.lapidist.colony.client.ui.AutosaveLabel;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.server.events.AutosaveEvent;
import net.lapidist.colony.tests.GdxTestRunner;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.file.Path;

import static org.junit.Assert.*;

/** Tests for {@link AutosaveLabel}. */
@RunWith(GdxTestRunner.class)
public class AutosaveLabelTest {

    private static final float ADVANCE = 2.5f;

    @Test
    public void showsAndHidesOnEvent() {
        EventSystem system = new EventSystem();
        Events.init(system);
        Skin skin = new Skin(Gdx.files.internal("skin/default.json"));
        AutosaveLabel label = new AutosaveLabel(skin);

        Events.dispatch(new AutosaveEvent(Path.of("tmp"), 1L));
        Events.update();
        assertTrue(label.isVisible());
        assertEquals("Saving...", label.getText().toString());

        label.act(ADVANCE);
        assertFalse(label.isVisible());
    }
}
