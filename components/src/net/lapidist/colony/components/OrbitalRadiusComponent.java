package net.lapidist.colony.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class OrbitalRadiusComponent implements Component {

    private Vector2 origin;
    private Vector2 radius;
    private Vector2 orbitalPosition;
    private float angle;

    public final Vector2 getOrigin() {
        return origin;
    }

    public final void setOrigin(final Vector2 originToSet) {
        this.origin = originToSet;
    }

    public final Vector2 getRadius() {
        return radius;
    }

    public final void setRadius(final Vector2 radiusToSet) {
        this.radius = radiusToSet;
    }

    public final Vector2 getOrbitalPosition() {
        return orbitalPosition;
    }

    public final void setOrbitalPosition(final Vector2 orbitalPositionToSet) {
        this.orbitalPosition = orbitalPositionToSet;
    }

    public final float getAngle() {
        return angle;
    }

    public final void setAngle(final float angleToSet) {
        this.angle = angleToSet;
    }
}
