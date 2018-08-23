package net.lapidist.colony.utils;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ThreadArray<T> extends Array<T> {
    private ThreadLocal<ArrayIterable<T>> iterableThreadLocal = new ThreadLocal<>();

    public ThreadArray() {
    }

    public ThreadArray(boolean ordered) {
        super(ordered, 16);
    }

    public ThreadArray(int capacity) {
        super(capacity);
    }

    @Override
    public Iterator<T> iterator() {
        if (iterableThreadLocal.get() == null) {
            iterableThreadLocal.set(new ArrayIterable<>(this));
        }

        return iterableThreadLocal.get().iterator();
    }
}
