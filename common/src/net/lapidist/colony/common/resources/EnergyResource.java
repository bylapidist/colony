package net.lapidist.colony.common.resources;

public class EnergyResource<T extends IResource> extends IResource {

    public EnergyResource(float initialValue) {
        super(initialValue);
    }

    @Override
    public String getName() {
        return "Energy";
    }

    @Override
    public void update() {

    }
}
