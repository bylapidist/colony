package net.lapidist.colony.core.modules;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.lapidist.colony.common.modules.Module;
import net.lapidist.colony.core.input.InputManager;

public class SceneModule extends Module {

    protected Stage stage;

    protected SceneModule() {
        stage = new Stage(new ScreenViewport());
        InputManager.add(stage);
    }

    private void act() {
        stage.act();
        stage.draw();
    }

    @Override
    public void update() {
        act();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
