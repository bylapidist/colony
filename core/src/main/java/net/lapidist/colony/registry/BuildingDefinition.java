package net.lapidist.colony.registry;

import net.lapidist.colony.components.state.ResourceData;

/**
 * Metadata describing a building type.
 *
 * @param id     unique identifier
 * @param label  display label
 * @param asset  rendering asset reference
 * @param cost   resources required to construct the building
 */
public record BuildingDefinition(String id, String label, String asset, ResourceData cost) {
    public BuildingDefinition() {
        this(null, null, null, new ResourceData());
    }

    /** Convenience constructor omitting cost (defaults to zero). */
    public BuildingDefinition(final String idValue, final String labelValue, final String assetValue) {
        this(idValue, labelValue, assetValue, new ResourceData());
    }
}
