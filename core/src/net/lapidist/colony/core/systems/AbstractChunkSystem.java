package net.lapidist.colony.core.systems;

import com.artemis.BaseSystem;

public abstract class AbstractChunkSystem extends BaseSystem {

    public abstract IChunk getActiveChunk();



    @Override
    protected void processSystem() {

    }
}
