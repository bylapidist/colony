package net.lapidist.colony.base;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.EnvironmentCycleService;
import net.lapidist.colony.components.state.EnvironmentState;

/** Built-in mod registering the season cycle service. */
public final class BaseSeasonCycleMod implements GameMod {
    private static final long TICK_PERIOD_MS = 1000L;
    private static final float SEASON_LENGTH_SECONDS = 60f;
    @Override
    public void registerSystems(final GameServer srv) {
        net.lapidist.colony.server.GameServer s = (net.lapidist.colony.server.GameServer) srv;
        srv.registerSystem(new EnvironmentCycleService(
                TICK_PERIOD_MS,
                s::getMapState,
                s::setMapState,
                s.getStateLock(),
                new java.util.function.BiFunction<EnvironmentState, Float, EnvironmentState>() {
                    private float elapsed;

                    @Override
                    public EnvironmentState apply(final EnvironmentState env, final Float dt) {
                        elapsed += dt;
                        if (elapsed < SEASON_LENGTH_SECONDS) {
                            return env;
                        }
                        elapsed -= SEASON_LENGTH_SECONDS;
                        return new EnvironmentState(env.timeOfDay(), env.season().next(), env.moonPhase());
                    }
                }
        ));
    }
}
