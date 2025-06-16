package net.lapidist.colony.components.state;

/**
 * Mutable runtime representation of {@link EnvironmentState}.
 */
public final class MutableEnvironmentState {
    private float timeOfDay;
    private Season season;
    private float moonPhase;

    public MutableEnvironmentState() {
        this(new EnvironmentState());
    }

    public MutableEnvironmentState(final EnvironmentState state) {
        this.timeOfDay = state.timeOfDay();
        this.season = state.season();
        this.moonPhase = state.moonPhase();
    }

    public float getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(final float time) {
        this.timeOfDay = time;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(final Season s) {
        this.season = s;
    }

    public float getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(final float phase) {
        this.moonPhase = phase;
    }

    /** Convert back to the immutable record type. */
    public EnvironmentState toImmutable() {
        return new EnvironmentState(timeOfDay, season, moonPhase);
    }
}
