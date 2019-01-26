package net.lapidist.colony.core.events.render;

import net.lapidist.colony.core.events.AbstractEvent;

public class AssetRegisterEvent<T> extends AbstractEvent {

    public AssetRegisterEvent(String path, Class<T> type) {
        super(
                "path=" + path
                        + ", type=" + type.getSimpleName()
        );
    }

}
