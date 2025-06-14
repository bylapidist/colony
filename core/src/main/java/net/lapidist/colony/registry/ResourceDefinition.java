package net.lapidist.colony.registry;

/**
 * Metadata describing a resource type.
 *
 * @param id    unique identifier
 * @param label display label
 * @param asset rendering asset reference
 */
public record ResourceDefinition(String id, String label, String asset) {
    public ResourceDefinition() {
        this(null, null, null);
    }
}
