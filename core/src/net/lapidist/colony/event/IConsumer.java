package net.lapidist.colony.event;

public interface IConsumer<T> {
    public void accept(T t);
}
