package net.lapidist.colony.components.assets;

import com.artemis.Component;

public abstract class ResourceReferenceComponent extends Component {

    private String resourceRef;

    public final String getResourceRef() {
        return resourceRef;
    }

    public final void setResourceRef(final String resourceRefToSet) {
        this.resourceRef = resourceRefToSet;
    }
}
