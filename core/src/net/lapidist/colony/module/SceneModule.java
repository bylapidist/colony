package net.lapidist.colony.module;

import net.lapidist.colony.core.Core;
import net.lapidist.colony.scene.Scene;

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
