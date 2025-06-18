package net.lapidist.colony.tests.server;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import net.lapidist.colony.components.state.map.MapState;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.io.TestPathService;
import net.lapidist.colony.server.services.AutosaveService;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class AutosaveServiceTest {

    @Test
    public void logsErrorWhenSaveFails() throws Exception {
        Path temp = Files.createTempDirectory("autosave-test");
        Paths paths = new Paths(new TestPathService(temp));
        Path autosave = paths.getAutosave("err");
        Files.createDirectories(autosave); // create directory to trigger failure

        Logger logger = (Logger) LoggerFactory.getLogger(AutosaveService.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        try (MockedStatic<Paths> mock = Mockito.mockStatic(Paths.class)) {
            mock.when(Paths::get).thenReturn(paths);
            AutosaveService service = new AutosaveService(
                    0,
                    "err",
                    MapState::new,
                    java.util.List::of,
                    new ReentrantLock()
            );
            service.stop();
        }

        logger.detachAppender(appender);
        assertEquals(1, appender.list.size());
        ILoggingEvent event = appender.list.get(0);
        assertEquals(Level.ERROR, event.getLevel());
    }
}
