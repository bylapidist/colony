package net.lapidist.colony.tests.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.graphics.ShaderManager;
import net.lapidist.colony.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class ShaderManagerTest {

    @Test
    public void loadsShaderProgram() throws IOException {
        File vertex = File.createTempFile("test", ".vert");
        File fragment = File.createTempFile("test", ".frag");
        String vert = String.join("\n",
                "attribute vec4 a_position;",
                "void main(){",
                "    gl_Position = a_position;",
                "}",
                "");
        String frag = String.join("\n",
                "#ifdef GL_ES",
                "precision mediump float;",
                "#endif",
                "void main(){",
                "    gl_FragColor = vec4(1.0);",
                "}",
                "");
        Files.writeString(vertex.toPath(), vert);
        Files.writeString(fragment.toPath(), frag);

        ShaderProgram program = Mockito.mock(ShaderProgram.class);
        Mockito.when(program.isCompiled()).thenReturn(true);

        ShaderManager manager = new ShaderManager((v, f) -> program);
        ShaderProgram loaded = manager.load(FileLocation.ABSOLUTE,
                vertex.getAbsolutePath(), fragment.getAbsolutePath());
        assertTrue(loaded.isCompiled());
        loaded.dispose();
        vertex.delete();
        fragment.delete();
    }
}
