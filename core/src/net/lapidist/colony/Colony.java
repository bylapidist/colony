package net.lapidist.colony;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitfire.utils.ShaderLoader;
import net.lapidist.colony.entity.EntityManager;
import net.lapidist.colony.screen.GameScreen;
import net.lapidist.colony.screen.ScreenTransition;
import net.lapidist.colony.screen.SplashScreen;
import net.lapidist.colony.screen.TransitionEffect;
import net.lapidist.colony.tween.Accessors;

public class Colony extends Game {

    public static Colony game;
    public SpriteBatch batch;
    public EntityManager entityManager;
    public TweenManager tweenManager;
    public TransitionEffect transitioner;
    private FPSLogger fpsLogger;

    @Override
    public void create() {
        game = this;

        batch = new SpriteBatch();
        entityManager = new EntityManager();
        tweenManager = new TweenManager();
        transitioner = new TransitionEffect();
        fpsLogger = new FPSLogger();

        // Register tween accessors and postprocessing
        Accessors.register();
        ShaderLoader.BasePath = "shader/postprocessing/";

        // Load splash screen
        if (!Constants.SKIP_SPLASH) {
            new ScreenTransition(new SplashScreen(), 1f);
        } else {
            new ScreenTransition(new GameScreen(), 1f);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        entityManager.dispose();
        transitioner.dispose();
        game = null;
    }

    @Override
    public void render() {
        super.render();
        fpsLogger.log();
    }

    public static Colony instance() {
        return game;
    }
}
