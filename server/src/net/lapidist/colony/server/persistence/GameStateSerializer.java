package net.lapidist.colony.server.persistence;

import java.io.File;
import java.io.IOException;

public interface GameStateSerializer<T> {
    void serialize(T state, File file) throws IOException;

    T deserialize(File file) throws IOException, ClassNotFoundException;
}
