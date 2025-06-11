package net.lapidist.colony.io;

import java.nio.file.Path;

/** Test helper PathService that resolves paths under a provided directory. */
public final class TestPathService extends AbstractPathService {
    private final Path base;

    public TestPathService(final Path baseDir) {
        this.base = baseDir;
    }

    @Override
    protected String getGameFolderPath() {
        return base.toString();
    }
}
