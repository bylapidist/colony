package net.lapidist.colony.core.events.payloads;

import com.badlogic.gdx.utils.Json;

public abstract class AbstractEventPayload {

    public String toJson() {
        Json json = new Json();
        return json.toJson(this);
    }

    public abstract AbstractEventPayload fromJson(final String json);

    public String toString() {
        return toJson();
    }
}
