package net.lapidist.colony.client.events;

import net.mostlyoriginal.api.event.common.Event;
import net.mostlyoriginal.api.event.common.EventSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Events {

    private static EventSystem instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(Events.class);

    private Events() {
    }

    public static void init(final EventSystem system) {
        instance = system;
    }

    public static EventSystem getInstance() {
        return instance;
    }

    public static void dispatch(final Event event) {
        if (instance != null) {
            LOGGER.debug("Dispatched event: {}", event);
            instance.dispatch(event);
        }
    }

    public static void update() {
        if (instance != null) {
            instance.process();
        }
    }

    public static void dispose() {
        instance = null;
    }
}
