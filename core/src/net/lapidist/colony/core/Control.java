package net.lapidist.colony.core;

import com.badlogic.gdx.Gdx;
import net.lapidist.colony.event.EventType;
import net.lapidist.colony.event.Events;
import net.lapidist.colony.module.Module;

public class Control extends Module {

    public Control() {
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void init() {
        super.init();

        Events.on(EventType.TileClickEvent.class, event -> {
            Core.camera.tweenToTile(event.tile);
            System.out.println("Clicked " + event.tile.hex.getId());
        });
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void update() {
        super.update();
    }
}
