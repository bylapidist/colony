package net.lapidist.colony.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Registry for {@link TileDefinition} instances. */
public final class TileRegistry {
    private final Map<String, TileDefinition> definitions = new HashMap<>();

    /** Register a new tile definition. */
    public void register(final TileDefinition def) {
        if (def == null || def.id() == null) {
            return;
        }
        definitions.put(def.id().toUpperCase(Locale.ROOT), def);
    }

    /** Lookup a tile definition by id. */
    public TileDefinition get(final String id) {
        if (id == null) {
            return null;
        }
        return definitions.get(id.toUpperCase(Locale.ROOT));
    }

    /** @return all registered definitions. */
    public Collection<TileDefinition> all() {
        return definitions.values();
    }
}
