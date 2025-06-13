package net.lapidist.colony.tests.components;

import net.lapidist.colony.components.state.MapState;
import net.lapidist.colony.components.state.ResourceData;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class MapStateBuilderTest {
    @Test
    public void builderCopiesValues() {
        MapState.Builder builder = MapState.builder()
                .version(2)
                .name("n")
                .saveName("s")
                .autosaveName("a")
                .description("d")
                .chunks(new HashMap<>())
                .buildings(List.of())
                .playerResources(new ResourceData());
        MapState state = builder.build();
        assertEquals(2, state.version());
        assertEquals("n", state.name());
        assertEquals("s", state.saveName());
        assertEquals("a", state.autosaveName());
        assertEquals("d", state.description());
    }
}
