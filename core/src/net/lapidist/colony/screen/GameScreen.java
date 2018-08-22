package net.lapidist.colony.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import net.lapidist.colony.Colony;
import net.lapidist.colony.Constants;
import net.lapidist.colony.input.MapInputProcessor;
import net.lapidist.colony.map.FlatMap;
import net.lapidist.colony.map.Map;

public class GameScreen implements Screen {

    private Colony game;
    private OrthographicCamera camera;
    private Map map;
    private PostProcessor postprocessor;

    public static final float SCROLL_SPEED = 10;
    public static final float ZOOM_SPEED = 0.05f;

    public GameScreen() {
        game = Colony.instance();

        // Postprocessing
        postprocessor = new PostProcessor(false, true, false);
        Bloom bloom = new Bloom((int)(1280 * 0.35f), (int)(720 * 0.35f));
        bloom.setBaseIntesity(2f);
        postprocessor.addEffect(bloom);

        // Set up cameras
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.setProjectionMatrix(camera.combined);

        // Input processing
        MapInputProcessor inputProcessor = new MapInputProcessor(this);

        Gdx.input.setInputProcessor(inputProcessor);

        // Generate a map
        map = new FlatMap(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, Constants.MAP_DEPTH);

        // Center the camera
        camera.position.x = map.getBoundX() / 2;
        camera.position.y = map.getBoundY() / 8;
    }

    @Override
    public void render(float delta) {
        // Update entities
        game.entityManager.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0.01f, 0.01f, 0.01f, 1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Input
        handleInput();

        // Update camera
        game.batch.setProjectionMatrix(camera.combined);
        camera.update();
        if (camera.zoom > 3.5f)
            camera.zoom = 3.5f;
        if (camera.zoom < 0.5f)
            camera.zoom = 0.5f;

        // Begin sprite batch
        game.batch.begin();

        // Draw entities
        game.entityManager.render(delta);
        game.batch.end();

        // Render fade screen
        game.transitioner.draw();

        // Update tween manager
        game.tweenManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        game.entityManager.resize(width, height);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        postprocessor.dispose();
        game.entityManager.dispose();
    }

    /**
     * Input handling for keyboards (events for Android and scroll wheel are in MapInputProcessor).
     */
    private void handleInput() {
        // Zoom out
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if (camera.zoom < 3.5f)
                camera.zoom += ZOOM_SPEED;
        }

        // Zoom in
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            if (camera.zoom > 0.5f)
                camera.zoom -= ZOOM_SPEED;
        }

        // Move left
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (camera.position.x > 0)
                camera.translate(-SCROLL_SPEED, 0, 0);
        }

        // Move right
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (camera.position.x < map.getBoundX())
                camera.translate(SCROLL_SPEED, 0, 0);
        }

        // Move down
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (camera.position.y > -map.getBoundY()/1.5f)
                camera.translate(0, -SCROLL_SPEED, 0);
        }

        // Move up
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (camera.position.y < map.getBoundY())
                camera.translate(0, SCROLL_SPEED, 0);
        }
    }

    /**
     * @return The camera.
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * @return The map.
     */
    public Map getMap() {
        return map;
    }
}
