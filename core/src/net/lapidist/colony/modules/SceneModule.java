package net.lapidist.colony.modules;

import net.lapidist.colony.core.Core;
import net.lapidist.colony.scenes.Scene;

public class SceneModule extends Module {

    public Scene scene;

    public SceneModule() {
        scene = new Scene(Core.spriteBatch);
    }

    public void act() {
        scene.act();
        scene.draw();
    }

    @Override
    public void update() {
        act();
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }
}
