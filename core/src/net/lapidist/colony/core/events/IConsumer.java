package net.lapidist.colony.core.events;

public interface IConsumer<T> {
    public void accept(T t);
}
