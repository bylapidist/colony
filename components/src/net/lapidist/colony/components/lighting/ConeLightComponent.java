package net.lapidist.colony.components.lighting;

import box2dLight.ConeLight;
import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class ConeLightComponent extends Component {

    private Array<ConeLight> coneLights;

    public ConeLightComponent() {
        coneLights = new Array<>();
    }

    public Array<ConeLight> getConeLights() {
        return coneLights;
    }
}

