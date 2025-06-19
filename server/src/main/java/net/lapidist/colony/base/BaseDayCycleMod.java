package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.EnvironmentCycleService;
import net.lapidist.colony.components.GameConstants;
import net.lapidist.colony.components.state.EnvironmentState;

/** Built-in mod registering the day/night cycle service. */
public final class BaseDayCycleMod implements GameMod {
    private static final long TICK_PERIOD_MS = 1000L;
    private static final float HOURS_PER_DAY = 24f;
    private static final float DAY_LENGTH_SECONDS = GameConstants.DAY_LENGTH_SECONDS;

    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        srv.registerSystem(new EnvironmentCycleService(
                TICK_PERIOD_MS,
                s::getMapState,
                s::setMapState,
                s.getStateLock(),
                (env, dt) -> {
                    float increment = (dt * HOURS_PER_DAY) / DAY_LENGTH_SECONDS;
                    float time = ((env.timeOfDay() + increment) % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
                    return new EnvironmentState(time, env.season(), env.moonPhase());
                }
        ));
    }
}
