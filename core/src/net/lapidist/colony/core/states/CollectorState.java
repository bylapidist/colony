package net.lapidist.colony.core.states;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import static com.artemis.E.*;

public enum CollectorState implements State<Entity> {
    IDLE() {
        @Override
        public void enter(Entity e) {
            super.enter(e);
        }
    },

    WANDERING() {
        @Override
        public void enter(Entity e) {
            super.enter(e);
        }
    };

    @Override
    public void enter(Entity e) {
    }

    @Override
    public void update(Entity e) {
    }

    @Override
    public void exit(Entity e) {
    }

    @Override
    public boolean onMessage(Entity e, Telegram telegram) {
        return false;
    }
}
