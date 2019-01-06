package net.lapidist.colony.core.core;

import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.common.modules.Module;
import net.lapidist.colony.core.events.EventType.TickEvent;
import net.lapidist.colony.core.events.EventType.WorldInitEvent;

import static net.lapidist.colony.core.Constants.state;

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
