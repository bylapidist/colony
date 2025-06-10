package net.lapidist.colony.io;

import com.badlogic.gdx.Gdx;
import java.nio.file.Path;

/**
 * Resolves the game folder path using LibGDX's {@code Files} API when
 * available. If {@code Gdx.files} is {@code null} (for example on the
 * server) the provided fallback path is used instead.
 */
final class GdxPathAdapter {

    private GdxPathAdapter() {
    }

    /**
     * Resolve the game folder path. When running inside a LibGDX
     * environment {@link Gdx#files} is used to determine the external
     * storage directory. Otherwise the supplied fallback path is returned.
     *
     * @param fallback path to use when LibGDX is not available
     * @return resolved game folder path
     */
    static Path resolveGameFolder(final Path fallback) {
        if (Gdx.files != null) {
            return Gdx.files.external(".colony").file().toPath();
        }
        return fallback;
    }
}
