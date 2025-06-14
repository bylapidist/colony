package net.lapidist.colony.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Registry for {@link BuildingDefinition} instances. */
public final class BuildingRegistry {
    private final Map<String, BuildingDefinition> definitions = new HashMap<>();

    /** Register a new building definition. */
    public void register(final BuildingDefinition def) {
        if (def == null || def.id() == null) {
            return;
        }
        definitions.put(def.id().toUpperCase(Locale.ROOT), def);
    }

    /** Lookup a building definition by id. */
    public BuildingDefinition get(final String id) {
        if (id == null) {
            return null;
        }
        return definitions.get(id.toUpperCase(Locale.ROOT));
    }

    /** @return all registered definitions. */
    public Collection<BuildingDefinition> all() {
        return definitions.values();
    }
}
