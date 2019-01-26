package net.lapidist.colony.core.events.time;

import net.lapidist.colony.core.events.AbstractEvent;
import net.lapidist.colony.core.systems.logic.TimeSystem;

public class SeasonChangeEvent extends AbstractEvent {

    private TimeSystem.Season season;

    public SeasonChangeEvent(TimeSystem.Season season) {
        super("season=" + season);

        setSeason(season);
    }

    public TimeSystem.Season getSeason() {
        return season;
    }

    public void setSeason(TimeSystem.Season season) {
        this.season = season;
    }
}
