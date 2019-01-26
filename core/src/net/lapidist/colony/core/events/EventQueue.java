package net.lapidist.colony.core.events;

import java.util.ArrayList;
import java.util.List;

/**
 * The EventQueue is a List of events, which are then run() sequentially.
 *
 * @param <T>
 */
public class EventQueue<T extends IEvent> {

    private List<T> queue;

    public EventQueue() {
        this.queue = new ArrayList<>();
    }

    /**
     * Add a new Event to the queue.
     *
     * @param event
     */
    public void add(IEvent event) {
        queue.add((T) event);
    }

    /**
     * Run all events sequentially.
     */
    public void run() {
        queue.forEach(Events::fire);
    }
}
