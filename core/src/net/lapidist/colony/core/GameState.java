package net.lapidist.colony.core;

public class GameState {

    private State state = State.MENU;

    public void set(State state) {
//        Events.fire(new StateChangeEvent(this.state, state));
        this.state = state;
    }

    public boolean isPaused() {
        return is(State.PAUSED);
    }

    public boolean is(State state) {
        return this.state == state;
    }

    public State getState() {
        return state;
    }

    public enum State {
        PAUSED, PLAYING, MENU
    }
}
