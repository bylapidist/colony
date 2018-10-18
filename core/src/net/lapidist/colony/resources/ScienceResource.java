package net.lapidist.colony.resources;

public class ScienceResource<T extends IResource> extends IResource {

    public ScienceResource(float initialValue) {
        super(initialValue);
    }

    @Override
    public String getName() {
        return "Science";
    }

    @Override
    public void update() {

    }
}
