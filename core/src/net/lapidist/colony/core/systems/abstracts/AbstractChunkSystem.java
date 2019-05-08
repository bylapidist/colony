package net.lapidist.colony.core.systems.abstracts;

import com.artemis.BaseSystem;
import net.lapidist.colony.core.systems.chunks.IChunk;

public abstract class AbstractChunkSystem extends BaseSystem {

    public abstract IChunk getActiveChunk();



    @Override
    protected void processSystem() {

    }
}
