package net.lapidist.colony.core.events;

import com.badlogic.gdx.Gdx;
import net.lapidist.colony.core.Constants;

public abstract class AbstractEvent implements IEvent {

    protected AbstractEvent(String payload) {
        if (Constants.DEBUG) {
            Gdx.app.log(
                    "Event",
                    this.getClass().getSimpleName() + " {" + payload + "}"
            );
        }
    }
}
