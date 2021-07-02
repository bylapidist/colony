package net.lapidist.colony.core.events;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;

public class PriorityQueue<T extends Comparable<T>> {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    private static final double CAPACITY_RATIO_LOW = 1.5f;

    private static final double CAPACITY_RATIO_HI = 2f;

    private Object[] queue;

    private final ObjectSet<T> set;

    private int size = 0;

    public PriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public PriorityQueue (final int initialCapacity) {
        this.queue = new Object[initialCapacity];
        this.set = new ObjectSet<>(initialCapacity);
    }

    @SuppressWarnings("unchecked")
    public T get(final int index) {
        return index >= this.size ? null : (T)this.queue[index];
    }

    public int size() {
        return this.size;
    }

    public void clear() {
        for (int i = 0; i < this.size; i++)
            this.queue[i] = null;
        this.size = 0;
        this.set.clear();
    }

    public boolean add(final T e) {
        if (e == null) throw new IllegalArgumentException("Element cannot be null.");

        int i = this.size;
        if (i >= this.queue.length)
            growToSize(i + 1);

        this.size = i + 1;

        if (i == 0)
            this.queue[0] = e;
        else
            siftUp(i, e);

        return true;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, final T x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            T e = (T)this.queue[parent];

            if (x.compareTo(e) >= 0) break;

            this.queue[k] = e;
            k = parent;
        }

        this.queue[k] = x;
    }

    private void growToSize(final int minCapacity) {
        if (minCapacity < 0)
            throw new GdxRuntimeException("Capacity upper limit exceeded.");

        int oldCapacity = this.queue.length;
        int newCapacity = (int)((oldCapacity < 64) ? ((oldCapacity + 1) * CAPACITY_RATIO_HI) : (oldCapacity * CAPACITY_RATIO_LOW));

        if (newCapacity < 0)
            newCapacity = Integer.MAX_VALUE;

        if (newCapacity < minCapacity)
            newCapacity = minCapacity;

        Object[] newQueue = new Object[newCapacity];
        System.arraycopy(queue, 0, newQueue, 0, this.size);
        this.queue = newQueue;
    }
}
