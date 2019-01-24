package net.lapidist.colony.core.systems.factories;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

@Wire
public final class LightFactorySystem extends BaseSystem {

    public PointLight createPointlight(RayHandler rayHandler, Body body, Color color, float intensity) {
        PointLight pointLight = new PointLight(rayHandler, 128, color, intensity, 0, 0);
        pointLight.setSoftnessLength(0f);
        pointLight.attachToBody(body);
        pointLight.setXray(false);
        pointLight.setStaticLight(false);
        pointLight.setSoft(true);

        return pointLight;
    }

    @Override
    protected void processSystem() {

    }
}
