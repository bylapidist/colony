package net.lapidist.colony.client.core.events;

public enum EventType {
    GAME_INIT(0, "Game Init"),
    PAUSE(1, "Pause"),
    RESUME(2, "Resume"),
    RESIZE(3, "Resize"),
    HIDE(4, "Hide"),
    SHOW(5, "Show"),
    DISPOSE(6, "Dispose");

    private final int ordinal;

    private final String name;

    EventType(final int ordinalToSet, final String nameToSet) {
        this.ordinal = ordinalToSet;
        this.name = nameToSet;
    }

    public int getOrdinal() {
        return this.ordinal;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", getOrdinal(), getName());
    }
}
