package net.lapidist.colony.core.events.time;

import net.lapidist.colony.core.events.AbstractEvent;
import net.lapidist.colony.core.systems.logic.TimeSystem;

public class TimeChangeEvent extends AbstractEvent {

    private TimeSystem.TimeOfDay timeOfDay;

    public TimeChangeEvent(TimeSystem.TimeOfDay timeOfDay) {
        super("timeOfDay=" + timeOfDay +
                '}');

        setTimeOfDay(timeOfDay);
    }

    public TimeSystem.TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(TimeSystem.TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
}
