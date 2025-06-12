package net.lapidist.colony.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

import static org.mockito.Mockito.*;

/**
 * Initializes a LibGDX headless application for JMH benchmarks.
 */
public final class GdxBenchmarkEnvironment {

    private static boolean initialized;

    private GdxBenchmarkEnvironment() {
    }

    /**
     * Boot LibGDX in headless mode and mock the GL20 context.
     */
    public static synchronized void init() {
        if (initialized) {
            return;
        }
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() { }, config);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;

        when(Gdx.gl20.glCreateShader(anyInt())).thenReturn(1);
        when(Gdx.gl20.glCreateProgram()).thenReturn(1);
        doAnswer(inv -> {
            java.nio.IntBuffer buf = (java.nio.IntBuffer) inv.getArguments()[2];
            buf.put(0, 1);
            return null;
        }).when(Gdx.gl20).glGetShaderiv(anyInt(), anyInt(), any());
        doAnswer(inv -> {
            java.nio.IntBuffer buf = (java.nio.IntBuffer) inv.getArguments()[2];
            buf.put(0, 1);
            return null;
        }).when(Gdx.gl20).glGetProgramiv(anyInt(), anyInt(), any());
        when(Gdx.gl20.glGetShaderInfoLog(anyInt())).thenReturn("");
        when(Gdx.gl20.glGetProgramInfoLog(anyInt())).thenReturn("");
        doNothing().when(Gdx.gl20).glShaderSource(anyInt(), any());
        doNothing().when(Gdx.gl20).glCompileShader(anyInt());
        doNothing().when(Gdx.gl20).glAttachShader(anyInt(), anyInt());
        doNothing().when(Gdx.gl20).glLinkProgram(anyInt());
        when(Gdx.gl20.glGetAttribLocation(anyInt(), any())).thenReturn(0);
        when(Gdx.gl20.glGetUniformLocation(anyInt(), any())).thenReturn(0);
        doNothing().when(Gdx.gl20).glUseProgram(anyInt());
        doNothing().when(Gdx.gl20).glDeleteShader(anyInt());
        doNothing().when(Gdx.gl20).glDeleteProgram(anyInt());
        initialized = true;
    }
}
