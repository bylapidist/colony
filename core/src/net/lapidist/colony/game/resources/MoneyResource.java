package net.lapidist.colony.game.resources;

public class MoneyResource<T extends IResource> extends IResource {

    public MoneyResource(float initialValue) {
        super(initialValue);
    }

    @Override
    public String getName() {
        return "Money";
    }

    @Override
    public void update() {

    }
}
