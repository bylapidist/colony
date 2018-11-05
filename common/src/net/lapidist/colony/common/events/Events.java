package net.lapidist.colony.common.events;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Events {

    private static ObjectMap<Class<? extends IEvent>, Array<IConsumer<? extends IEvent>>> events = new ObjectMap<>();

    public static <T extends IEvent> void on(Class<T> type, IConsumer<T> listener) {
        if (events.get(type) == null) {
            events.put(type, new Array<>());
        }

        events.get(type).add(listener);
    }

    public static <T extends IEvent> void fire(T type) {
        if (events.get(type.getClass()) == null) {
            return;
        }

        for (IConsumer<? extends IEvent> event : events.get(type.getClass())) {
            ((IConsumer<T>)event).accept(type);
        }
    }
}
