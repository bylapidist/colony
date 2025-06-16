package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import net.lapidist.colony.components.state.MutableEnvironmentState;
import net.lapidist.colony.components.state.Season;

/**
 * Cycles the game season after a fixed interval of real time.
 */
public final class SeasonCycleSystem extends BaseSystem {
    private final MutableEnvironmentState environment;
    private final float seasonLength;
    private float elapsed;

    /**
     * @param env          environment to update
     * @param lengthInSecs duration of a season in seconds
     */
    public SeasonCycleSystem(final MutableEnvironmentState env, final float lengthInSecs) {
        this.environment = env;
        this.seasonLength = lengthInSecs;
    }

    /** Default constructor using 60 seconds per season. */
    public SeasonCycleSystem(final MutableEnvironmentState env) {
        this(env, 60f);
    }

    @Override
    protected void processSystem() {
        elapsed += world.getDelta();
        if (elapsed >= seasonLength) {
            elapsed -= seasonLength;
            environment.setSeason(environment.getSeason().next());
        }
    }
}
