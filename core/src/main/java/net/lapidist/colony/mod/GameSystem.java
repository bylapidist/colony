package net.lapidist.colony.mod;

/** Simple lifecycle for server side runnable systems. */
public interface GameSystem {
    /** Start the system. */
    void start();
    /** Stop the system. */
    void stop();
}
