package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.lapidist.colony.client.core.io.FileLocation;

import java.io.IOException;

/**
 * Utility for loading and compiling shader programs.
 */
import java.util.function.BiFunction;

public final class ShaderManager {

    private final BiFunction<FileHandle, FileHandle, ShaderProgram> factory;

    public ShaderManager() {
        this(ShaderProgram::new);
    }

    public ShaderManager(final BiFunction<FileHandle, FileHandle, ShaderProgram> factoryToUse) {
        this.factory = factoryToUse;
    }

    /**
     * Load and compile a shader program from the given file paths.
     *
     * @param location the location type for resolving files
     * @param vertexPath path to the vertex shader source
     * @param fragmentPath path to the fragment shader source
     * @return the compiled {@link ShaderProgram}
     * @throws IOException if sources are missing or compilation fails
     */
    public ShaderProgram load(
            final FileLocation location,
            final String vertexPath,
            final String fragmentPath
    ) throws IOException {
        FileHandle vertexFile = location.getFile(vertexPath);
        FileHandle fragmentFile = location.getFile(fragmentPath);
        if (!vertexFile.exists() || !fragmentFile.exists()) {
            throw new IOException("Shader source not found");
        }
        ShaderProgram program = factory.apply(vertexFile, fragmentFile);
        if (!program.isCompiled()) {
            String log = program.getLog();
            program.dispose();
            throw new IOException(log);
        }
        return program;
    }
}
