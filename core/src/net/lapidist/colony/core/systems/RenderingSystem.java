package net.lapidist.colony.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.lapidist.colony.core.Constants;

public class RenderingSystem extends IteratingSystem {

    public RenderingSystem() {
        super(Family.all().get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    public void draw() {
        Constants.engine.getSystem(MapRenderingSystem.class).draw();
    }
}
