package net.lapidist.colony.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Registry for {@link ItemDefinition} instances. */
public final class ItemRegistry {
    private final Map<String, ItemDefinition> definitions = new HashMap<>();

    /** Register a new item definition. */
    public void register(final ItemDefinition def) {
        if (def == null || def.id() == null) {
            return;
        }
        definitions.put(def.id().toUpperCase(Locale.ROOT), def);
    }

    /** Lookup an item definition by id. */
    public ItemDefinition get(final String id) {
        if (id == null) {
            return null;
        }
        return definitions.get(id.toUpperCase(Locale.ROOT));
    }

    /** @return all registered definitions. */
    public Collection<ItemDefinition> all() {
        return definitions.values();
    }
}
