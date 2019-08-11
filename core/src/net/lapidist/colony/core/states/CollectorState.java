package net.lapidist.colony.core.states;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum CollectorState implements State<Entity> {
    IDLE() {
        @Override
        public void enter(Entity entity) {
            super.enter(entity);
        }
    };

    private static final String TAG = "[CollectorState]";

    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void update(Entity entity) {

    }

    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
