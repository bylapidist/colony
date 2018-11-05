package net.lapidist.colony.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;

public class PlayerSystem extends IteratingSystem {

    private Array<Entity> players;

    public PlayerSystem() {
        super(Family.all().get());

        players = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (!players.contains(entity, true)) {
            players.add(entity);
        }
    }
}
