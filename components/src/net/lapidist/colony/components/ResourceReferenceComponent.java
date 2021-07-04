package net.lapidist.colony.components;

import com.badlogic.ashley.core.Component;

public abstract class ResourceReferenceComponent implements Component {

    private String resourceRef;

    public final String getResourceRef() {
        return resourceRef;
    }

    public final void setResourceRef(final String resourceRefToSet) {
        this.resourceRef = resourceRefToSet;
    }
}
