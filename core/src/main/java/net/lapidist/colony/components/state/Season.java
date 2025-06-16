package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/** Enumerates the possible world seasons. */
@KryoType
public enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER;

    /** Next season in the cycle. */
    public Season next() {
        Season[] values = values();
        return values[(ordinal() + 1) % values.length];
    }
}
