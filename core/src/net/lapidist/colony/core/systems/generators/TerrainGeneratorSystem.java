package net.lapidist.colony.core.systems.generators;

import com.artemis.annotations.Wire;
import net.lapidist.colony.core.systems.abstracts.AbstractGeneratorSystem;
import net.lapidist.colony.core.systems.factories.EntityFactorySystem;

import static com.artemis.E.E;

@Wire
public class TerrainGeneratorSystem extends AbstractGeneratorSystem {

    private EntityFactorySystem entityFactorySystem;

    public TerrainGeneratorSystem(String seed, int width, int height) {
        super(seed, width, height);
    }

    @Override
    protected void initialize() {
//        for (int ty = 0; ty < getHeight(); ty++) {
//            for (int tx = 0; tx < getWidth(); tx++) {
//                map[tx][ty] = entityFactorySystem.create(entityFactorySystem.getArchetype("tile"));
//
//                E(map[tx][ty]).tileComponentTile(tileWidth, tileHeight);
//                E(map[tx][ty]).worldPositionComponentPosition(new Vector3(tx, ty, 0));
//                E(map[tx][ty]).originComponentOrigin(new Vector2(0.5f, 0.5f));
//            }
//        }
    }

    @Override
    protected void processSystem() {

    }
}
