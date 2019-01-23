package net.lapidist.colony.core.systems.abstracts;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.utils.Bag;
import net.lapidist.colony.core.systems.delegate.EntityProcessAgent;
import net.lapidist.colony.core.systems.delegate.EntityProcessPrincipal;
import net.lapidist.colony.core.systems.render.RenderBatchingSystem;

import java.util.Arrays;

import static com.artemis.E.E;

public abstract class AbstractBatchingSystem extends BaseSystem implements EntityProcessPrincipal {

    private final Bag<Job> sortedJobs = new Bag<>();
    private boolean sortedDirty = false;

    @Override
    protected void setWorld(World world) {
        super.setWorld(world);
    }

    private void sort(Bag bag) {
        if (!bag.isEmpty()) {
            Arrays.sort(bag.getData(), 0, bag.size());
        }
    }

    @Override
    public void registerAgent(int e, EntityProcessAgent agent) {
        if (!E(e).hasSortableComponent())
            throw new RuntimeException("BatchingSystems require agent entities to have SortableComponent.");
        sortedJobs.add(new Job(e, agent));
        sortedDirty = true;
    }

    @Override
    public void unregisterAgent(int entityId, EntityProcessAgent agent) {
        final Object[] data = sortedJobs.getData();

        for (int i = 0, s = sortedJobs.size(); i < s; i++) {
            final RenderBatchingSystem.Job e2 = (RenderBatchingSystem.Job) data[i];
            if (e2.entityId == entityId && e2.agent == agent) {
                sortedJobs.remove(i);
                sortedDirty = true;
                break;
            }
        }
    }

    @Override
    protected void processSystem() {
        if (sortedDirty) {
            sortedDirty = false;
            sort(sortedJobs);
        }

        EntityProcessAgent activeAgent = null;
        final Object[] data = sortedJobs.getData();
        for (int i = 0, s = sortedJobs.size(); i < s; i++) {
            final Job job = (Job) data[i];
            final EntityProcessAgent agent = job.agent;

            if (agent != activeAgent) {
                if (activeAgent != null) {
                    activeAgent.end();
                }
                activeAgent = agent;
                activeAgent.begin();
            }

            agent.process(job.entityId);
        }

        if (activeAgent != null) {
            activeAgent.end();
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    public class Job implements Comparable<Job> {
        final int entityId;
        final EntityProcessAgent agent;

        Job(final int entityId, final EntityProcessAgent agent) {
            this.entityId = entityId;
            this.agent = agent;
        }

        @Override
        public int compareTo(Job o) {
            return E(this.entityId).sortableComponentLayer() - E(o.entityId).sortableComponentLayer();
        }
    }
}
