package net.lapidist.colony.client.events;

import net.mostlyoriginal.api.event.common.Event;

/** Event to adjust simulation speed. */
public record SpeedMultiplierEvent(double multiplier) implements Event {
}
