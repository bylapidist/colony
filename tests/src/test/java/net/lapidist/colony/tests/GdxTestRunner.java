package net.lapidist.colony.tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GdxTestRunner
        extends BlockJUnit4ClassRunner implements ApplicationListener {

    private static final int MILLIS = 10;

    private final Map<FrameworkMethod, RunNotifier> invokeInRender
            = new HashMap<>();

    public GdxTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);

        HeadlessApplicationConfiguration config =
                new HeadlessApplicationConfiguration();
        new HeadlessApplication(this, config);
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
    }

    private void waitUntilInvokedInRenderMethod() {
        try {
            while (true) {
                Thread.sleep(MILLIS);
                synchronized (invokeInRender) {
                    if (invokeInRender.isEmpty()) {
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected final void runChild(
            final FrameworkMethod method,
            final RunNotifier notifier
    ) {
        synchronized (invokeInRender) {
            invokeInRender.put(method, notifier);
        }
        waitUntilInvokedInRenderMethod();
    }

    @Override
    public void create() {
    }

    @Override
    public void resize(final int width, final int height) {
    }

    @Override
    public final void render() {
        synchronized (invokeInRender) {
            for (
                    Map.Entry<FrameworkMethod, RunNotifier> each
                    : invokeInRender.entrySet()
            ) {
                super.runChild(each.getKey(), each.getValue());
            }
            invokeInRender.clear();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
