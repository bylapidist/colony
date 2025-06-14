package net.lapidist.colony.registry;

/**
 * Metadata describing a building type.
 *
 * @param id     unique identifier
 * @param label  display label
 * @param asset  rendering asset reference
 */
public record BuildingDefinition(String id, String label, String asset) {
    public BuildingDefinition() {
        this(null, null, null);
    }
}
