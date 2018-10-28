package net.lapidist.colony.core;

import net.lapidist.colony.events.EventType.*;
import net.lapidist.colony.events.Events;
import net.lapidist.colony.modules.Module;

import static net.lapidist.colony.Constants.*;

public class Logic extends Module {

    public boolean doUpdate = true;

    public Logic() {
        state = new GameState();
    }

    @Override
    public void init() {
        Events.on(WorldInitEvent.class, worldInitEvent -> {
            System.out.println("World Initialized");
        });

        play();
    }

    @Override
    public void update() {
        if (doUpdate) {
            Events.fire(new TickEvent());
        }
    }

    public void play() {
        state.set(GameState.State.PLAYING);
    }
}
