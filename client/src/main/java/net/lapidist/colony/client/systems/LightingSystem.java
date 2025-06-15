package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.graphics.LightMapProvider;

/** Builds a simple light map each frame. */
public final class LightingSystem extends BaseSystem implements LightMapProvider, Disposable {
    private FrameBuffer buffer;
    private TextureRegion region;
    private PlayerCameraSystem camera;

    @Override
    public void initialize() {
        camera = world.getSystem(PlayerCameraSystem.class);
        int w = (int) camera.getViewport().getWorldWidth();
        int h = (int) camera.getViewport().getWorldHeight();
        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
        region = new TextureRegion(buffer.getColorBufferTexture());
        region.flip(false, true);
    }

    @Override
    protected void processSystem() {
        buffer.begin();
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // placeholder: real lighting would render occluders and lights here
        buffer.end();
    }

    @Override
    public Texture getLightMapTexture() {
        return buffer.getColorBufferTexture();
    }

    @Override
    public void dispose() {
        buffer.dispose();
    }
}
