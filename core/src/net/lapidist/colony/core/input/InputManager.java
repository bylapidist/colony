package net.lapidist.colony.core.input;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public final class InputManager {

    private static Array<InputProcessor> processors = new Array<>();

    public static void add(InputProcessor processor) {
        processors.add(processor);
    }

    public static void remove(InputProcessor processor) {
        processors.removeValue(processor, true);
    }

    public static void clear() {
        processors.clear();
    }

    public static InputMultiplexer getMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        processors.forEach(multiplexer::addProcessor);
        return multiplexer;
    }
}
