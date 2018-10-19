package net.lapidist.colony.events;

public interface IConsumer<T> {
    public void accept(T t);
}
