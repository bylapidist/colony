package net.lapidist.colony.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Registry for {@link ResourceDefinition} instances. */
public final class ResourceRegistry {
    private final Map<String, ResourceDefinition> definitions = new HashMap<>();

    /** Register a new resource definition. */
    public void register(final ResourceDefinition def) {
        if (def == null || def.id() == null) {
            return;
        }
        definitions.put(def.id().toUpperCase(Locale.ROOT), def);
    }

    /** Lookup a resource definition by id. */
    public ResourceDefinition get(final String id) {
        if (id == null) {
            return null;
        }
        return definitions.get(id.toUpperCase(Locale.ROOT));
    }

    /** @return all registered definitions. */
    public Collection<ResourceDefinition> all() {
        return definitions.values();
    }
}
