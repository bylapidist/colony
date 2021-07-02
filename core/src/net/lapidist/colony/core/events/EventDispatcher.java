package net.lapidist.colony.core.events;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.reflect.ClassReflection;


public class EventDispatcher implements IEventListener {

    private static final Pool<Event> EVENT_POOL = new Pool<>(16) {
        @Override
        protected Event newObject() {
            return new Event();
        }
    };

    private final PriorityQueue<Event> queue;

    private final IntMap<Array<IEventListener>> eventListeners;

    private final IntMap<Array<IEventProvider>> eventProviders;

    private boolean debugModeEnabled;

    public EventDispatcher() {
        this.queue = new PriorityQueue<>();
        this.eventListeners = new IntMap<>();
        this.eventProviders = new IntMap<>();
    }

    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    public void setDebugModeEnabled(final boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
    }

    public void addListener(final IEventListener eventListener, final Event.EventType eventType) {
        Array<IEventListener> listeners = this.eventListeners.get(eventType.getType());

        if (listeners == null) {
            listeners = new Array<>(false, 16);
            this.eventListeners.put(eventType.getType(), listeners);
        }
        listeners.add(eventListener);

        Array<IEventProvider> providers = this.eventProviders.get(eventType.getType());

        if (providers != null) {
            for (int i = 0, n = providers.size; i < n; i++) {
                IEventProvider provider = providers.get(i);
                IEventPayload eventPayload = provider.provideEventPayload(eventType.getType(), eventListener);

                if (eventPayload != null) {
                    IEventListener eventSender = ClassReflection.isInstance(IEventListener.class, provider)
                            ? (IEventListener) provider
                            : null;
                    dispatch(0, eventSender, eventListener, eventType.getType(), eventPayload, false);
                }
            }
        }
    }

    public void addListeners(final IEventListener eventListener, final Event.EventType... eventTypes) {
        for (final Event.EventType eventType : eventTypes)
            addListener(eventListener, eventType);
    }

    public void addProvider(final IEventProvider eventProvider, final Event.EventType eventType) {
        Array<IEventProvider> providers = this.eventProviders.get(eventType.getType());

        if (providers == null) {
            providers = new Array<IEventProvider>(false, 16);
            this.eventProviders.put(eventType.getType(), providers);
        }
        providers.add(eventProvider);
    }

    public void addProviders(final IEventProvider eventProvider, final Event.EventType... eventTypes) {
        for (final Event.EventType eventType : eventTypes)
            addProvider(eventProvider, eventType);
    }

    public void removeListener(final IEventListener eventListener, final Event.EventType eventType) {
        Array<IEventListener> listeners = this.eventListeners.get(eventType.getType());
        if (listeners != null) {
            listeners.removeValue(eventListener, true);
        }
    }

    public void removeListener(final IEventListener eventListener, final Event.EventType... eventTypes) {
        for (final Event.EventType eventType : eventTypes)
            removeListener(eventListener, eventType);
    }

    public void clearListeners(final Event.EventType eventType) {
        eventListeners.remove(eventType.getType());
    }

    public void clearListeners(final Event.EventType... eventTypes) {
        for (final Event.EventType eventType : eventTypes)
            clearListeners(eventType);
    }

    public void clearAllListeners() {
        this.eventListeners.clear();
    }

    public void clearProviders(final Event.EventType eventType) {
        this.eventProviders.remove(eventType.getType());
    }

    public void clearProviders(final Event.EventType... eventTypes) {
        for (final Event.EventType eventType : eventTypes)
            clearProviders(eventType);
    }

    public void clearAllProviders() {
        this.eventProviders.clear();
    }

    public void clearQueue() {
        for (int i = 0; i < this.queue.size(); i++) {
            EVENT_POOL.free(this.queue.get(i));
        }
        this.queue.clear();
    }

    public void clearAll() {
        clearQueue();
        clearAllListeners();
        clearAllProviders();
    }

    public void dispatch(
        final float delay,
        final IEventListener eventSender,
        final IEventListener eventReceiver,
        final Event.EventType eventType,
        final IEventPayload eventPayload,
        final boolean needsReturnReceipt
    ) {
        if (eventSender == null && needsReturnReceipt)
            throw new IllegalArgumentException("Sender cannot be null when return receipt is needed");

        Event event = EVENT_POOL.obtain();
        event.sender = eventSender;
        event.reciever = eventReceiver;
        event.type = eventType;
        event.payload = eventPayload;
        event.returnReceiptStatus = needsReturnReceipt ? Event.RETURN_RECEIPT_NEEDED : Event.RETURN_RECEIPT_UNNEEDED;

        if (delay <= 0.0f) {
            if (debugModeEnabled) {
                System.out.printf("[EVENT] %s sent by %s for %s", eventType.toString(), eventSender, eventReceiver);
            }

            discharge(event);
        } else {
            boolean added = this.queue.add(event);

            if (!added)
                EVENT_POOL.free(event);

            if (debugModeEnabled) {
                if (added) {
                    System.out.printf("[DELAYED_EVENT] %s sent by %s for %s", eventType.toString(), eventSender, eventReceiver);
                } else {
                    System.out.printf("[REJECTED_EVENT] %s send by %s for %s was rejected", eventType.toString(), eventSender, eventReceiver);
                }
            }
        }
    }

    public void update() {
        Event event;

        while((event = queue.peek()) != null) {
            if ()
        }
    }

    @Override
    public boolean handleEvent(final Event event) {
        return false;
    }
}
