package net.lapidist.colony.core.io;

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
        switch (this) {
            case CLASSPATH:
                return Gdx.files.classpath(path);
            case INTERNAL:
                return Gdx.files.internal(path);
            case LOCAL:
                return Gdx.files.local(path);
            case EXTERNAL:
                return Gdx.files.external(path);
            case ABSOLUTE:
                return Gdx.files.absolute(path);
            default:
                return null;
        }
    }

    public final FileHandleResolver getResolver() {
        return this::getFile;
    }
}

