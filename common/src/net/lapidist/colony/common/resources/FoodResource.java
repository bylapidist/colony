package net.lapidist.colony.common.resources;

public class FoodResource<T extends IResource> extends IResource {

    public FoodResource(float initialValue) {
        super(initialValue);
    }

    @Override
    public String getName() {
        return "Food";
    }

    @Override
    public void update() {

    }
}
