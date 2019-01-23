package net.lapidist.colony.core.systems.delegate;

public interface EntityProcessAgent {

    void begin();

    void end();

    void process(int e);

}
