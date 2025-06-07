package net.lapidist.colony.client.events;

import net.mostlyoriginal.api.event.common.Event;
import net.mostlyoriginal.api.event.common.EventSystem;

public final class Events {

    private static EventSystem instance;

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
            System.out.printf(
                    "[%s] Dispatched event: %s%n",
                    Events.class.getSimpleName(),
                    event.toString()
            );
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
