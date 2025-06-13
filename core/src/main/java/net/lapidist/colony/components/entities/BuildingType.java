package net.lapidist.colony.components.entities;

import net.lapidist.colony.i18n.I18n;

/** Prototype describing a building type loaded from mods. */
public record BuildingType(String id, String key) {
    @Override
    public String toString() {
        return I18n.get(key);
    }
}
