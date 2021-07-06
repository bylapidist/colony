package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public enum FileLocation {
    CLASSPATH,
    INTERNAL,
    LOCAL,
    EXTERNAL,
    ABSOLUTE;

    public FileHandle getFile(final String path) {
        return switch (this) {
            case CLASSPATH -> Gdx.files.classpath(path);
            case INTERNAL -> Gdx.files.internal(path);
            case LOCAL -> Gdx.files.local(path);
            case EXTERNAL -> Gdx.files.external(path);
            case ABSOLUTE -> Gdx.files.absolute(path);
        };
    }

    public final FileHandleResolver getResolver() {
        return this::getFile;
    }
}

