package net.lapidist.colony.common.events;

public interface IConsumer<T> {
    public void accept(T t);
}
