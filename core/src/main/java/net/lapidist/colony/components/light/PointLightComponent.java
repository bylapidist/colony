package net.lapidist.colony.components.light;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/** Component describing a dynamic point light. */
public final class PointLightComponent extends Component {
    private Color color = new Color(1f, 1f, 1f, 1f);
    private float radius = 1f;
    private float intensity = 1f;

    /** Light color. */
    public Color getColor() {
        return color;
    }

    public void setColor(final Color colorToSet) {
        this.color = colorToSet;
    }

    /** Light radius. */
    public float getRadius() {
        return radius;
    }

    public void setRadius(final float radiusToSet) {
        this.radius = radiusToSet;
    }

    /** Light intensity represented by the alpha channel. */
    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(final float intensityToSet) {
        this.intensity = intensityToSet;
    }
}
