package net.lapidist.colony.core.events;

import net.lapidist.colony.common.events.IEvent;
import net.lapidist.colony.core.components.TileComponent;

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
