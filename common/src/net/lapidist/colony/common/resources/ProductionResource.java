package net.lapidist.colony.common.resources;

public class ProductionResource<T extends IResource> extends IResource {

    public ProductionResource(float initialValue) {
        super(initialValue);
    }

    @Override
    public String getName() {
        return "Production";
    }
    @Override
    public void update() {

    }
}
