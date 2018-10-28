package net.lapidist.colony.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Scene extends InputAdapter implements Disposable {

    private final Batch batch;
    private Viewport viewport;

    public Scene() {
        this(new SpriteBatch());
    }

    public Scene(Viewport viewport) {
        this(new SpriteBatch());
    }

    public Scene(Batch batch) {
        this.batch = batch;
        this.viewport = new ScreenViewport() {
            @Override
            public void calculateScissors(Matrix4 batchTransform, Rectangle area, Rectangle scissor) {
                super.calculateScissors(batchTransform, area, scissor);
            }
        };

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    public void draw() {
        Camera camera = viewport.getCamera();
        camera.update();

        Batch batch = this.batch;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.end();
    }

    public void act() {
        act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    }

    public void act(float delta) {

    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
