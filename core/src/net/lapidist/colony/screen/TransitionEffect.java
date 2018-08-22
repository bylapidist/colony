package net.lapidist.colony.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.lapidist.colony.Colony;

public class TransitionEffect {

    private ShapeRenderer renderer;
    private OrthographicCamera camera;
    private float opacity = 1;
    private boolean doneFading;

    public TransitionEffect() {
        Colony game = Colony.instance();

        camera = new OrthographicCamera();
        renderer = new ShapeRenderer();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
    }

    /**
     * Draw fade screen.
     */
    public void draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Draw black rectangle the size of the viewport
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, opacity);
        renderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
        renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    /**
     * Returns true if it's not changing opacity, false otherwise.
     *
     * @return True or false.
     */
    public boolean getDoneFading() {
        return doneFading;
    }

    /**
     * Tell the FadeScreen instance that it is done fading.
     *
     * @param doneFading True or false.
     */
    public void setDoneFading(boolean doneFading) {
        this.doneFading = doneFading;
    }

    /**
     * Get the current Opacity.
     *
     * @return Current opacity (0-1).
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Set the current opacity.
     *
     * @param opacity Opacity (0-1).
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    /**
     * Dispose of the ShapeRenderer.
     */
    public void dispose() {
        renderer.dispose();
    }
}
