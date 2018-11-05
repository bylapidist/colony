package net.lapidist.colony.core.core;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.lapidist.colony.core.modules.SceneModule;

import static net.lapidist.colony.core.Constants.*;

public class UI extends SceneModule {

    private Table table;
    private Skin skin;

    public UI() {
        skin = resourceLoader.getSkin("default");

        Label label = new Label("Turn", skin);
        TextField textField = new TextField("", skin);
        TextButton nextTurnButton = new TextButton("Next", skin);

        table = new Table();
        table.setFillParent(true);
        table.right().bottom();
//        table.setDebug(true);

        table.add(label);
        table.add(textField).width(200);
        table.add(nextTurnButton).width(100);
        table.toFront();

        stage.addActor(table);
    }


}
