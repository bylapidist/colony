package net.lapidist.colony.core.events.payloads;

import com.badlogic.gdx.utils.Json;

public abstract class AbstractEventPayload {

    public final String toJson() {
        Json json = new Json();
        return json.toJson(this);
    }

    public abstract AbstractEventPayload fromJson(String json);

    public final String toString() {
        return toJson();
    }
}
