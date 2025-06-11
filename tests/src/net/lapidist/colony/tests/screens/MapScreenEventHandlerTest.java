package net.lapidist.colony.tests.screens;

import net.lapidist.colony.client.events.*;
import net.lapidist.colony.client.screens.MapScreenEventHandler;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.tests.GdxTestRunner;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class MapScreenEventHandlerTest {

    private static final class Capture {
        private boolean show;
        private boolean hide;
        private boolean pause;
        private boolean resume;
        private boolean disposed;
        private ResizeEvent resize;

        @Subscribe
        public void onShow(final ShowEvent event) {
            show = true;
        }

        @Subscribe
        public void onHide(final HideEvent event) {
            hide = true;
        }

        @Subscribe
        public void onPause(final PauseEvent event) {
            pause = true;
        }

        @Subscribe
        public void onResume(final ResumeEvent event) {
            resume = true;
        }

        @Subscribe
        public void onDispose(final DisposeEvent event) {
            disposed = true;
        }

        @Subscribe
        public void onResize(final ResizeEvent event) {
            resize = event;
        }
    }

    private Capture capture;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @Before
    public void setup() {
        capture = new Capture();
        EventSystem system = new EventSystem();
        Events.init(system);
        system.registerEvents(capture);
    }

    @Test
    public void dispatchesLifecycleEvents() {
        MapScreenEventHandler handler = new MapScreenEventHandler();
        handler.show();
        handler.pause();
        handler.resume();
        handler.resize(WIDTH, HEIGHT);
        handler.hide();
        handler.dispose();
        Events.update();

        assertTrue(capture.show);
        assertTrue(capture.pause);
        assertTrue(capture.resume);
        assertTrue(capture.hide);
        assertTrue(capture.disposed);
        assertEquals(WIDTH, capture.resize.width());
        assertEquals(HEIGHT, capture.resize.height());
    }
}
