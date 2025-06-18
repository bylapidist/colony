package net.lapidist.colony.registry;

import net.lapidist.colony.components.state.resources.ResourceData;

/**
 * Metadata describing a building type.
 *
 * @param id     unique identifier
 * @param label  display label
 * @param asset  rendering asset reference
 * @param cost   resources required to construct the building
 * @param description short localized description
 */
public record BuildingDefinition(
        String id,
        String label,
        String asset,
        ResourceData cost,
        String description
) {
    public BuildingDefinition() {
        this(null, null, null, new ResourceData(), null);
    }

    /** Convenience constructor omitting cost (defaults to zero). */
    public BuildingDefinition(
            final String idValue,
            final String labelValue,
            final String assetValue
    ) {
        this(idValue, labelValue, assetValue, new ResourceData(), null);
    }

    /** Constructor with custom cost, no description. */
    public BuildingDefinition(
            final String idValue,
            final String labelValue,
            final String assetValue,
            final ResourceData costValue
    ) {
        this(idValue, labelValue, assetValue, costValue, null);
    }
}
