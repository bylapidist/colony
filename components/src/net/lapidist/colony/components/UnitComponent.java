package net.lapidist.colony.components;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;

public class UnitComponent extends Component {

    private State<Entity> state;

    public State<Entity> getState() {
        return state;
    }

    public void setState(State<Entity> state) {
        this.state = state;
    }
}
