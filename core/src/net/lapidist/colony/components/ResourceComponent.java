package net.lapidist.colony.components;

import com.badlogic.ashley.core.Component;
import net.lapidist.colony.resources.IResource;

import java.util.ArrayList;
import java.util.List;

public class ResourceComponent implements Component {

    private List<IResource> resources = new ArrayList<>();

    public void addResource(IResource resource) {
        resources.add(resource);
    }

    public List<IResource> getResources() {
        return resources;
    }
}
