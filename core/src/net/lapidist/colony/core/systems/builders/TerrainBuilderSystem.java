package net.lapidist.colony.core.systems.builders;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.systems.assets.MapAssetSystem;
import net.lapidist.colony.core.systems.factories.ArchetypeFactorySystem;
import net.lapidist.colony.core.systems.factories.Archetypes;
import net.lapidist.colony.core.systems.generators.MapGeneratorSystem;

import static com.artemis.E.E;

@Wire
public class TerrainBuilderSystem extends BaseSystem {

    private ArchetypeFactorySystem archetypeFactorySystem;
    private MapGeneratorSystem mapGeneratorSystem;
    private MapAssetSystem mapAssetSystem;

    public int createChunk(int x, int y) {
        int chunk = archetypeFactorySystem.create(archetypeFactorySystem.getArchetype(Archetypes.CHUNK));
        E(chunk).originComponentOrigin(new Vector2(x, y));
        E(chunk).worldPositionComponentPosition(
                new Vector3(
                        x * mapGeneratorSystem.getTileWidth(),
                        y * mapGeneratorSystem.getTileHeight(),
                        0
                )
        );
        createGrass(chunk);

        return chunk;
    }

    public void createGrass(int chunk) {
        int e = archetypeFactorySystem.create(archetypeFactorySystem.getArchetype(Archetypes.TERRAIN));

        E(e).textureComponentTexture(mapAssetSystem.getTexture("grass"));
        E(e).rotationComponentRotation(0);
        E(e).originComponentOrigin(new Vector2(0.5f, 0.5f));
        E(e).worldPositionComponentPosition(new Vector3(
                E(chunk).worldPositionComponentPosition().x,
                E(chunk).worldPositionComponentPosition().y,
                0
        ));
        E(e).scaleComponentScale(1);
        E(e).sortableComponentLayer(0);
    }

    @Override
    protected void processSystem() {
    }
}