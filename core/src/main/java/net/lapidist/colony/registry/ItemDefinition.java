package net.lapidist.colony.registry;

/**
 * Metadata describing an item type.
 *
 * @param id     unique identifier
 * @param label  display label
 * @param asset  rendering asset reference
 */
public record ItemDefinition(String id, String label, String asset) {
    public ItemDefinition() {
        this(null, null, null);
    }
}
