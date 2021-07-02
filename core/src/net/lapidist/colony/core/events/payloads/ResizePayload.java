package net.lapidist.colony.core.events.payloads;

import com.badlogic.gdx.utils.Json;

public class ResizePayload extends AbstractEventPayload {

    public int width;

    public int height;

    public ResizePayload(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public AbstractEventPayload fromJson(String jsonString) {
        Json json = new Json();
        return json.fromJson(ResizePayload.class, jsonString);
    }
}
