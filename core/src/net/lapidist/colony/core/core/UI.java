package net.lapidist.colony.core.core;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import net.lapidist.colony.core.modules.SceneModule;

import static net.lapidist.colony.core.Constants.resourceLoader;

public class UI extends SceneModule {

    public UI() {
        Skin skin = resourceLoader.getSkin("default");
        Label turnLabel = new Label("Turn", skin);
        Label turnLabelCount = new Label("1", skin);
        TextButton nextTurnButton = new TextButton("Next", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.right().bottom();
//        table.setDebug(true);

        table.add(turnLabel);
        table.add(turnLabelCount).width(50);
        table.add(nextTurnButton).width(100);
        table.toFront();

        stage.addActor(table);
    }


}
