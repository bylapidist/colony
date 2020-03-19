package net.lapidist.colony.core.systems.factories;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

@Wire
public final class LightFactorySystem extends BaseSystem {

    private static final int NUM_RAYS = 128;

    public PointLight createPointlight(
            final RayHandler rayHandler,
            final Body body,
            final Color color,
            final float intensity
    ) {
        PointLight pointLight = new PointLight(
                rayHandler,
                NUM_RAYS,
                color,
                intensity,
                0,
                0
        );
        pointLight.setSoftnessLength(0f);
        pointLight.attachToBody(body);
        pointLight.setXray(false);
        pointLight.setStaticLight(false);
        pointLight.setSoft(true);

        return pointLight;
    }

    public ConeLight createConeLight(
            final RayHandler rayHandler,
            final Body body,
            final Color color,
            final float intensity,
            final float direction,
            final float coneDegree
    ) {
        ConeLight coneLight = new ConeLight(
                rayHandler,
                NUM_RAYS,
                color,
                intensity,
                0,
                0,
                direction,
                coneDegree
        );
        coneLight.setSoftnessLength(0f);
        coneLight.attachToBody(body);
        coneLight.setXray(false);
        coneLight.setStaticLight(false);
        coneLight.setSoft(true);

        return coneLight;
    }

    @Override
    protected void processSystem() {

    }
}
