package net.lapidist.colony.events;

import net.lapidist.colony.components.TileComponent;

public class EventType {

    public static class GameLoadEvent implements IEvent {

    }

    public static class WorldInitEvent implements IEvent {
        public WorldInitEvent() {
//            Core.camera.zoom = 2f;
        }
    }

    public static class TickEvent implements IEvent {

    }

    public static class TileClickEvent implements IEvent {

        public final TileComponent tile;

        public TileClickEvent(TileComponent tile) {
            this.tile = tile;
        }
    }
}
