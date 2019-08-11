package net.lapidist.colony.core.systems.builders;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.CircleShape;
import net.lapidist.colony.core.states.CollectorState;
import net.lapidist.colony.core.systems.assets.MapAssetSystem;
import net.lapidist.colony.core.systems.factories.ArchetypeFactorySystem;
import net.lapidist.colony.core.systems.factories.Archetypes;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;
import net.lapidist.colony.core.systems.physics.MapPhysicsSystem;

import static com.artemis.E.E;

@Wire
public class UnitBuilderSystem extends BaseSystem {

    private ArchetypeFactorySystem archetypeFactorySystem;
    private MapGeneratorSystem mapGeneratorSystem;
    private MapAssetSystem mapAssetSystem;
    private MapPhysicsSystem mapPhysicsSystem;

    public int createCollector(int x, int y) {
        int e = archetypeFactorySystem.create(
                archetypeFactorySystem.getArchetype(Archetypes.COLLECTOR)
        );

        E(e).unitComponentState(CollectorState.IDLE);
        E(e).textureComponentTexture(mapAssetSystem.getTexture("dirt"));
        E(e).rotationComponentRotation(0);
        E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
        E(e).worldPositionComponentPosition(new Vector3(
                x * mapGeneratorSystem.getTileWidth(),
                y * mapGeneratorSystem.getTileHeight(),
                0
        ));
        E(e).scaleComponentScale(1);
        E(e).velocityComponentVelocity(new Vector2(0, 0));
        E(e).sortableComponentLayer(1);
        E(e).dynamicBodyComponentFixtureDef().shape = new CircleShape();
        E(e).dynamicBodyComponentFixtureDef().shape.setRadius(0.5f);
        E(e).dynamicBodyComponentBodyDef().position.set(
                E(e).worldPositionComponentPosition().x,
                E(e).worldPositionComponentPosition().y
        );
        E(e).dynamicBodyComponentBody(
                mapPhysicsSystem.getPhysicsWorld().createBody(
                        E(e).dynamicBodyComponentBodyDef()
                )
        );
        E(e).dynamicBodyComponentBody().createFixture(
                E(e).dynamicBodyComponentFixtureDef()
        );

        return e;
    }

    @Override
    protected void processSystem() {
    }
}