package net.lapidist.colony.client.core.events;

import com.badlogic.gdx.ai.msg.Telegraph;

public interface IListener extends Telegraph {
    void addMessageListeners();
}
