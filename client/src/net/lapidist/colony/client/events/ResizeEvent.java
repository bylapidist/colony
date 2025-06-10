package net.lapidist.colony.client.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 * Event fired when the window is resized.
 */
public record ResizeEvent(int width, int height) implements Event {

    public ResizeEvent {
        // explicit constructor for future validation
    }

    @Override
    public String toString() {
        return String.format(
                "%s(width=%d, height=%d)",
                getClass().getSimpleName(),
                width,
                height
        );
    }
}
