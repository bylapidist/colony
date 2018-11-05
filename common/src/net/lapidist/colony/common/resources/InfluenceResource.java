package net.lapidist.colony.common.resources;

public class InfluenceResource<T extends IResource> extends IResource {

    public InfluenceResource(float initialValue) {
        super(initialValue);
    }

    @Override
    public String getName() {
        return "Influence";
    }

    @Override
    public void update() {

    }
}
