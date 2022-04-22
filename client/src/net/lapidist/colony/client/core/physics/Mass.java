package net.lapidist.colony.client.core.physics;

import com.badlogic.gdx.math.Vector2;

public final class Mass {

    public static Vector2 applyForce(
            final Vector2 position,
            final Vector2 acceleration,
            final Vector2 force,
            final float mass
    ) {
        Vector2 newtonsLaw = new Vector2(force.x / mass, force.y / mass);
        acceleration.add(newtonsLaw);
        position.add(acceleration);

        return position;
    }
}
