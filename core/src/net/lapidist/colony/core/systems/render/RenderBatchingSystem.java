package net.lapidist.colony.core.systems.render;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.utils.Bag;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.core.systems.delegate.EntityProcessAgent;
import net.lapidist.colony.core.systems.delegate.EntityProcessPrincipal;

import java.util.Arrays;

@Wire
public class RenderBatchingSystem extends BaseSystem implements EntityProcessPrincipal {

	protected ComponentMapper<RenderableComponent> mRenderable;

	protected final Bag<Job> sortedJobs = new Bag<>();
	public boolean sortedDirty = false;

	@Override
	protected void setWorld(World world) {
		super.setWorld(world);
	}

	@Override
	public void registerAgent(int entityId, EntityProcessAgent agent) {
		if (!mRenderable.has(entityId))
			throw new RuntimeException("RenderBatchingSystem requires agents entities to have RenderableComponent.");
		sortedJobs.add(new Job(entityId, agent));
		sortedDirty = true;
	}

	@Override
	public void unregisterAgent(int entityId, EntityProcessAgent agent) {
		final Object[] data = sortedJobs.getData();

		for (int i = 0, s = sortedJobs.size(); i < s; i++) {
			final Job e2 = (Job) data[i];
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
		public final int entityId;
		public final EntityProcessAgent agent;

		public Job(final int entityId, final EntityProcessAgent agent) {
			this.entityId = entityId;
			this.agent = agent;
		}

		@Override
		public int compareTo(Job o) {
			return mRenderable.get(this.entityId).getLayer() - mRenderable.get(o.entityId).getLayer();
		}
	}

	private void sort(Bag bag) {
		if (!bag.isEmpty()) {
			Arrays.sort(bag.getData(), 0, bag.size());
		}
	}
}
