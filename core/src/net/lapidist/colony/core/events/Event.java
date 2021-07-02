package net.lapidist.colony.core.events;

public class Event {

    public enum EventType {
        GAME_INIT(0, "GAME_INIT");

        private final int type;
        private final String name;

        EventType(final int typeToSet, final String nameToSet) {
            this.type = typeToSet;
            this.name = nameToSet;
        }

        public int getType() {
            return type;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
