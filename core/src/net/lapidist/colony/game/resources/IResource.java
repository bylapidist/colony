package net.lapidist.colony.game.resources;

public abstract class IResource<T> {

    private float initialValue;
    private float value;
    private float computedValue;
    private float modifier;

    public IResource() {
        this(0f, 0f);
    }

    public IResource(float initialValue) {
        this(initialValue, 0f);
    }

    public IResource(float initialValue, float modifier) {
        this.initialValue = initialValue;
        this.modifier = modifier;
        this.computedValue = initialValue;
    }

    abstract public String getName();

    abstract public void update();

    public float getInitialValue() {
        return initialValue;
    }

    public float getValue() {
        return value;
    }

    public float getComputedValue() {
        return computedValue;
    }

    public float getModifier() {
        return modifier;
    }

    public IResource<T> getResource() {
        return this;
    }
}
