package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

/**
 * World environment information shared between server and clients.
 *
 * @param timeOfDay current time of day between 0 and 24
 * @param season    current season of the year
 * @param moonPhase phase of the moon between 0 and 1
 */
@KryoType
public record EnvironmentState(float timeOfDay, Season season, float moonPhase) {
    public EnvironmentState() {
        this(0f, Season.SPRING, 0f);
    }
}
