package net.lapidist.colony.screen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import net.lapidist.colony.Colony;
import net.lapidist.colony.tween.BloomEffectAccessor;

public class SplashScreen implements Screen {

    private Colony game;
    private Screen screen;
    private OrthographicCamera camera;
    private PostProcessor postProcessor;
    private Texture splashTexture;
    private Sound gongSound;
    private Bloom bloom;
    private boolean switchingScreens;

    public SplashScreen() {
        game = Colony.instance();
        screen = this;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.setProjectionMatrix(camera.combined);

        // Load resources (splash screen uses minimal resources,
        // no need to AssetManager these - for now).
        splashTexture = new Texture(Gdx.files.internal("ui/splash.png"));
        gongSound = Gdx.audio.newSound(Gdx.files.internal("sound/splash_gong.mp3"));

        // Post processing
        postProcessor = new PostProcessor(false, false, true);
        bloom = new Bloom((int)(camera.viewportWidth * 0.35f), (int)(camera.viewportHeight * 0.35f));

        Tween.set(bloom, BloomEffectAccessor.INTENSITY).target(5f).start(game.tweenManager);
        Tween.to(bloom, BloomEffectAccessor.INTENSITY, 0.6f).target(0.1f).start(game.tweenManager);
        postProcessor.addEffect(bloom);

        // Go to next screen after a delay
        Tween.call(nextScreen).delay(5f).start(game.tweenManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.1f, 1);

        // Update camera
        camera.update();

        // Postprocessing start
        postProcessor.capture();

        // Begin sprite batch
        game.batch.begin();

        // Draw splash texture
        game.batch.draw(splashTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);

        // End sprite batch
        game.batch.end();

        // Postprocessing end
        postProcessor.render();

        // Render fade screen
        game.transitioner.draw();

        // Update tween manager
        game.tweenManager.update(delta);

        // Switch to game screen if touched
        if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
//            new ScreenTransition(this, new GameScreen(), 1);
        }
    }

    TweenCallback nextScreen = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            if (type == START && !switchingScreens) {
                switchingScreens = true;
                new ScreenTransition(screen, new GameScreen(), 1);
            }
        }
    };

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        gongSound.play();
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
        gongSound.dispose();
        splashTexture.dispose();
    }
}
