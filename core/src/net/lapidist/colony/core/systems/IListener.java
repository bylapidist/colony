package net.lapidist.colony.core.systems;

import com.badlogic.gdx.ai.msg.Telegraph;

public interface IListener extends Telegraph {
    void addMessageListeners();
}
