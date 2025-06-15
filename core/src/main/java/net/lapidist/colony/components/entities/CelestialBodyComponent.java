package net.lapidist.colony.components.entities;

import com.artemis.Component;

/**
 * Component describing a celestial body sprite and its orbital parameters.
 */
public final class CelestialBodyComponent extends Component {
    private String texture;
    private float orbitRadius;
    private float orbitOffset;
    private float x;
    private float y;

    public String getTexture() {
        return texture;
    }

    public void setTexture(final String textureToSet) {
        this.texture = textureToSet;
    }

    public float getOrbitRadius() {
        return orbitRadius;
    }

    public void setOrbitRadius(final float radius) {
        this.orbitRadius = radius;
    }

    public float getOrbitOffset() {
        return orbitOffset;
    }

    public void setOrbitOffset(final float offset) {
        this.orbitOffset = offset;
    }

    public float getX() {
        return x;
    }

    public void setX(final float xToSet) {
        this.x = xToSet;
    }

    public float getY() {
        return y;
    }

    public void setY(final float yToSet) {
        this.y = yToSet;
    }
}
