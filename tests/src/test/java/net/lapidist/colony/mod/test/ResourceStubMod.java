package net.lapidist.colony.mod.test;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.GameServer;
import net.lapidist.colony.server.services.ResourceProductionService;
import org.mockito.stubbing.Answer;

import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/** Mod that replaces the server resource production service with a mock. */
public final class ResourceStubMod implements GameMod {
    /** Count of start() invocations on the stub service. */
    public static final AtomicInteger START_CALLS = new AtomicInteger();
    /** Last created stub service instance. */
    private static ResourceProductionService service;

    @Override
    public void registerServices(final GameServer server) {
        service = mock(ResourceProductionService.class);
        doAnswer((Answer<Void>) inv -> {
            START_CALLS.incrementAndGet();
            return null;
        }).when(service).start();
        server.setResourceProductionServiceFactory(() -> service);
    }

    @Override
    public void registerSystems(final GameServer server) {
        server.registerSystem(service);
    }
}
