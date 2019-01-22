package net.lapidist.colony.core.events.render;

import net.lapidist.colony.core.events.AbstractEvent;

public class TextureRegisterEvent extends AbstractEvent {

    public TextureRegisterEvent(String name) {
        super(
                "name=" + name
        );
    }

}
