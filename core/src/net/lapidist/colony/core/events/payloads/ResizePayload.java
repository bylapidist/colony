package net.lapidist.colony.core.events.payloads;

import com.badlogic.gdx.utils.Json;

public final class ResizePayload extends AbstractEventPayload {

    private int width;

    private int height;

    public ResizePayload(final int widthToSet, final int heightToSet) {
        setWidth(widthToSet);
        setHeight(heightToSet);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(final int widthToSet) {
        this.width = widthToSet;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(final int heightToSet) {
        this.height = heightToSet;
    }

    @Override
    public AbstractEventPayload fromJson(final String jsonString) {
        Json json = new Json();
        return json.fromJson(ResizePayload.class, jsonString);
    }
}
