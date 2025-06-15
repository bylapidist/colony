package net.lapidist.colony.tests.core.io;

import net.lapidist.colony.io.PathService;
import net.lapidist.colony.io.Paths;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PathsTest {


    @Test
    public void delegatesGetSave() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("save.dat");
        when(service.getSave("save")).thenReturn(expected);
        Paths paths = new Paths(service);

        Path actual = paths.getSave("save");
        assertEquals(expected, actual);
        verify(service).getSave("save");
    }

    @Test
    public void delegatesGetAutosave() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("save" + Paths.AUTOSAVE_SUFFIX);
        when(service.getAutosave("save")).thenReturn(expected);
        Paths paths = new Paths(service);

        Path actual = paths.getAutosave("save");
        assertEquals(expected, actual);
        verify(service).getAutosave("save");
    }

    @Test
    public void delegatesListAutosaves() throws Exception {
        PathService service = mock(PathService.class);
        List<String> expected = Arrays.asList("a", "b");
        when(service.listAutosaves()).thenReturn(expected);
        Paths paths = new Paths(service);

        List<String> actual = paths.listAutosaves();
        assertEquals(expected, actual);
        verify(service).listAutosaves();
    }

    @Test
    public void delegatesDeleteAutosave() throws Exception {
        PathService service = mock(PathService.class);
        Paths paths = new Paths(service);

        paths.deleteAutosave("temp");
        verify(service).deleteAutosave("temp");
    }

    @Test
    public void delegatesCreateGameFoldersIfNotExists() throws Exception {
        PathService service = mock(PathService.class);
        Paths paths = new Paths(service);

        paths.createGameFoldersIfNotExists();
        verify(service).createGameFoldersIfNotExists();
    }

    @Test
    public void delegatesGetSettingsFile() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("settings.properties");
        when(service.getSettingsFile()).thenReturn(expected);
        Paths paths = new Paths(service);

        Path actual = paths.getSettingsFile();
        assertEquals(expected, actual);
        verify(service).getSettingsFile();
    }

    @Test
    public void delegatesGetLastAutosaveMarker() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("marker.dat");
        when(service.getLastAutosaveMarker()).thenReturn(expected);
        Paths paths = new Paths(service);

        Path actual = paths.getLastAutosaveMarker();
        assertEquals(expected, actual);
        verify(service).getLastAutosaveMarker();
    }

    @Test
    public void delegatesGetConfigFile() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("config.conf");
        when(service.getConfigFile()).thenReturn(expected);
        Paths paths = new Paths(service);

        Path actual = paths.getConfigFile();
        assertEquals(expected, actual);
        verify(service).getConfigFile();
    }

    @Test
    public void delegatesGetModsFolder() throws Exception {
        PathService service = mock(PathService.class);
        Path expected = java.nio.file.Paths.get("mods");
        when(service.getModsFolder()).thenReturn(expected);
        Paths paths = new Paths(service);

        Path actual = paths.getModsFolder();
        assertEquals(expected, actual);
        verify(service).getModsFolder();
    }
}
