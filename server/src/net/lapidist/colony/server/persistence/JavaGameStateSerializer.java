package net.lapidist.colony.server.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class JavaGameStateSerializer<T> implements GameStateSerializer<T> {
    @Override
    public void serialize(final T state, final File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(state);
        }
    }

    @Override
    public T deserialize(final File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            T obj = (T) in.readObject();
            return obj;
        }
    }
}
