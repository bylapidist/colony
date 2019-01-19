package net.lapidist.colony.components;

import box2dLight.PointLight;
import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class PointLightComponent extends Component {

    private Array<PointLight> pointLights;

    public PointLightComponent() {
        pointLights = new Array<>();
    }

    public Array<PointLight> getPointLights() {
        return pointLights;
    }
}
