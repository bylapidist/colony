package net.lapidist.colony.core;

import net.lapidist.colony.event.EventType.WorldInitEvent;
import net.lapidist.colony.event.EventType.TickEvent;
import net.lapidist.colony.event.Events;
import net.lapidist.colony.module.Module;

import static net.lapidist.colony.Constants.state;

public class Logic extends Module {

    public boolean doUpdate = true;

    public Logic() {
        state = new GameState();

        Events.on(WorldInitEvent.class, worldInitEvent -> {
            System.out.println("World Initialized");
        });
    }

    @Override
    public void init() {
    }

    @Override
    public void update() {
        Events.fire(new TickEvent());
    }

    public void play() {
        state.set(GameState.State.PLAYING);
    }

    public void reset() {
    }
}
