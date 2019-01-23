package net.lapidist.colony.core.systems.delegate;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;

public abstract class DeferredEntityProcessingSystem extends BaseEntitySystem {

    private final Aspect.Builder aspect;
    private final EntityProcessPrincipal principal;

    public DeferredEntityProcessingSystem(Aspect.Builder aspect, EntityProcessPrincipal principal) {
        super(aspect);
        this.aspect = aspect;
        this.principal = principal;
    }
    
    @Override
    protected void initialize() {
    	super.initialize();
    	
    	setEnabled(false);
    }

   	protected abstract void process(int e);

    @Override
    protected void removed(int entityId) {

        // inform delegation handler
        principal.unregisterAgent(entityId, localProcessingAgent);

        super.removed(entityId);
    }

    @Override
    protected void inserted(int entityId) {
        super.inserted(entityId);

        // warn delegation handler we've lost interest in this entity.
        principal.registerAgent(entityId, localProcessingAgent);
    }

	@Override
	protected void processSystem() {
	}

    protected EntityProcessAgent localProcessingAgent = new EntityProcessAgent() {
        @Override
        public void begin() {
            DeferredEntityProcessingSystem.this.begin();
        }

        @Override
        public void end() {
            DeferredEntityProcessingSystem.this.end();
        }

        @Override
        public void process(int e) {
            DeferredEntityProcessingSystem.this.process(e);
        }
    };
}
