package net.lapidist.colony.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import static net.lapidist.colony.Constants.*;

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
        engine.getSystem(MapRenderingSystem.class).draw();
    }
}
