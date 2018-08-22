package net.lapidist.colony.screen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Screen;
import net.lapidist.colony.Colony;
import net.lapidist.colony.tween.TransitionEffectAccessor;

public class ScreenTransition {

    private Colony game;
    private Screen from;
    private Screen to;
    private float duration;

    /**
     * Fades from black to the first screen. Should only ever be used with the splash screen.
     * @param screen Screen to fade to.
     * @param duration Duration in seconds.
     */
    public ScreenTransition(Screen screen, float duration) {
        this.game = Colony.instance();
        this.duration = duration;

        game.transitioner.setDoneFading(false);
        game.setScreen(screen);
        fadeIn();
    }

    /**
     * Fades to black, switches screen, fades back in to the new screen.
     * @param from From this screen.
     * @param to To this screen.
     * @param duration Duration in seconds.
     */
    public ScreenTransition(Screen from, Screen to, float duration) {
        this.game = Colony.instance();
        this.from = from;
        this.to = to;
        this.duration = duration / 2; // We half the duration, since each fade is done individually

        fadeOut();
    }

    /**
     * Fade screen in (lower opacity).
     */
    private void fadeIn() {
        Tween.set(game.transitioner, TransitionEffectAccessor.ALPHA)
                .target(1)
                .start(game.tweenManager);
        Tween.to(game.transitioner, TransitionEffectAccessor.ALPHA, duration)
                .target(0)
                .setCallback(setDoneFading)
                .start(game.tweenManager);
    }

    /**
     * Fade screen out (increase opacity).
     */
    private void fadeOut() {
        if (game.transitioner.getDoneFading()) {
            game.transitioner.setDoneFading(false);
            Tween.set(game.transitioner, TransitionEffectAccessor.ALPHA)
                    .target(0)
                    .start(game.tweenManager);
            Tween.to(game.transitioner, TransitionEffectAccessor.ALPHA, duration)
                    .target(1)
                    .setCallback(switchScreen)
                    .start(game.tweenManager);
        }
    }

    /**
     * After fading to black, switch the screen, then begin fading back in.
     */
    TweenCallback switchScreen = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            if (type == COMPLETE)
                game.transitioner.setDoneFading(true);
            from.dispose();
            game.batch.flush();
            game.setScreen(to);
            fadeIn();
        }
    };

    /**
     * When the tween is complete, setDoneFading to true.
     */
    TweenCallback setDoneFading = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            if (type == COMPLETE)
                game.transitioner.setDoneFading(true);
        }
    };
}
