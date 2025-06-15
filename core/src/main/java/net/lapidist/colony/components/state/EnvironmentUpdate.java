package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/** Message broadcasting the latest environment state. */
@KryoType
public record EnvironmentUpdate(EnvironmentState state) {
    public EnvironmentUpdate() {
        this(new EnvironmentState());
    }
}
