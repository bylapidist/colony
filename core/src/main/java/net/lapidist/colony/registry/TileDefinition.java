package net.lapidist.colony.registry;

/**
 * Metadata describing a tile type.
 *
 * @param id     unique identifier
 * @param label  display label
 * @param asset  rendering asset reference
 */
public record TileDefinition(String id, String label, String asset) {
    public TileDefinition() {
        this(null, null, null);
    }
}
