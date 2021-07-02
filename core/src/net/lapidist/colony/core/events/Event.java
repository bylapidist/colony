package net.lapidist.colony.core.events;

import com.badlogic.gdx.ai.msg.Telegram;
import net.lapidist.colony.core.events.payloads.AbstractEventPayload;

public class Event extends Telegram {

    public AbstractEventPayload payload;

    public Event(final AbstractEventPayload payload) {
        this.payload = payload;
    }

    @Override
    public void reset() {
        super.reset();
        this.payload = null;
    }

    @Override
    public boolean equals(Object obj) {
        Event other = (Event)obj;
        return super.equals(obj) && this.payload.equals(other.payload);
    }
}
