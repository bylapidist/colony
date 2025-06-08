package net.lapidist.colony.tests.core.io;

import net.lapidist.colony.io.PathService;
import net.lapidist.colony.io.Paths;
import org.junit.After;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PathsTest {

    @After
    public void tearDown() {
        Paths.setService(Paths.createDefaultService());
    }

    @Test
    public void delegatesGetSave() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("save.dat");
        when(service.getSave("save")).thenReturn(expected);
        Paths.setService(service);

        Path actual = Paths.getSave("save");
        assertEquals(expected, actual);
        verify(service).getSave("save");
    }

    @Test
    public void delegatesGetAutosave() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("save" + Paths.AUTOSAVE_SUFFIX);
        when(service.getAutosave("save")).thenReturn(expected);
        Paths.setService(service);

        Path actual = Paths.getAutosave("save");
        assertEquals(expected, actual);
        verify(service).getAutosave("save");
    }

    @Test
    public void delegatesListAutosaves() throws Exception {
        PathService service = mock(PathService.class);
        List<String> expected = Arrays.asList("a", "b");
        when(service.listAutosaves()).thenReturn(expected);
        Paths.setService(service);

        List<String> actual = Paths.listAutosaves();
        assertEquals(expected, actual);
        verify(service).listAutosaves();
    }

    @Test
    public void delegatesDeleteAutosave() throws Exception {
        PathService service = mock(PathService.class);
        Paths.setService(service);

        Paths.deleteAutosave("temp");
        verify(service).deleteAutosave("temp");
    }

    @Test
    public void delegatesCreateGameFoldersIfNotExists() throws Exception {
        PathService service = mock(PathService.class);
        Paths.setService(service);

        Paths.createGameFoldersIfNotExists();
        verify(service).createGameFoldersIfNotExists();
    }

    @Test
    public void delegatesGetSettingsFile() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("settings.properties");
        when(service.getSettingsFile()).thenReturn(expected);
        Paths.setService(service);

        Path actual = Paths.getSettingsFile();
        assertEquals(expected, actual);
        verify(service).getSettingsFile();
    }
}
