package net.lapidist.colony.client.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.renderers.PostProcessor;

/**
 * Simple bloom/blur processor using two FrameBuffers.
 */
public final class BloomPostProcessor implements PostProcessor, Disposable {

    private FrameBuffer sceneBuffer;
    private FrameBuffer pingBuffer;
    private final SpriteBatch batch;
    private int width;
    private int height;

    public BloomPostProcessor() {
        this(new SpriteBatch());
    }

    public BloomPostProcessor(final SpriteBatch spriteBatch) {
        this.batch = spriteBatch;
    }

    @Override
    public void resize(final int w, final int h) {
        this.width = w;
        this.height = h;
        if (sceneBuffer != null) {
            sceneBuffer.dispose();
        }
        if (pingBuffer != null) {
            pingBuffer.dispose();
        }
        sceneBuffer = new FrameBuffer(com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888, w, h, false);
        pingBuffer = new FrameBuffer(com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888, w, h, false);
    }

    @Override
    public void begin() {
        if (sceneBuffer == null || sceneBuffer.getWidth() != width || sceneBuffer.getHeight() != height) {
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        sceneBuffer.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void end() {
        sceneBuffer.end();
        Texture tex = sceneBuffer.getColorBufferTexture();
        pingBuffer.begin();
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
        batch.begin();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                batch.draw(tex, x, y, width, height, 0, 0, 1, 1);
            }
        }
        batch.end();
        pingBuffer.end();

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        batch.draw(pingBuffer.getColorBufferTexture(), 0, 0, width, height, 0, 0, 1, 1);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (sceneBuffer != null) {
            sceneBuffer.dispose();
        }
        if (pingBuffer != null) {
            pingBuffer.dispose();
        }
    }
}
