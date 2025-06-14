package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.server.io.GameStateIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LoadGameScreen extends BaseScreen {
    private static final float PADDING = 10f;
    private final Colony colony;

    public LoadGameScreen(final Colony game) {
        this.colony = game;

        Table list = new Table();

        List<String> saves = listSaves();
        for (String save : saves) {
            TextButton loadButton = new TextButton(save, getSkin());
            TextButton deleteButton = new TextButton(I18n.get("loadGame.delete"), getSkin());
            Table row = new Table();
            row.add(loadButton).padRight(PADDING);
            row.add(deleteButton);
            list.add(row).row();

            loadButton.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    try {
                        var meta = GameStateIO.readMetadata(Paths.get().getAutosave(save));
                        colony.startGame(save, meta.width(), meta.height());
                    } catch (IOException e) {
                        colony.startGame(save);
                    }
                }
            });

            deleteButton.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    Dialog confirm = new Dialog(I18n.get("loadGame.dialogTitle"), getSkin(), "dialog") {
                        @Override
                        protected void result(final Object obj) {
                            if (Boolean.TRUE.equals(obj)) {
                                try {
                                    Paths.get().deleteAutosave(save);
                                } catch (IOException e) {
                                    // ignore
                                }
                                colony.setScreen(new LoadGameScreen(colony));
                            }
                        }
                    };
                    confirm.text(I18n.get("loadGame.confirm"));
                    confirm.button(I18n.get("common.yes"), true);
                    confirm.button(I18n.get("common.no"), false);
                    confirm.show(getStage());
                }
            });
        }

        if (saves.isEmpty()) {
            list.add(new Label(I18n.get("loadGame.none"), getSkin())).row();
        }

        ScrollPane scroll = new ScrollPane(list, getSkin());
        scroll.setScrollingDisabled(true, false);
        getRoot().add(scroll).expand().fill().row();

        TextButton backButton = new TextButton(I18n.get("common.back"), getSkin());
        getRoot().add(backButton).row();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
            }
        });

        getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    colony.setScreen(new MainMenuScreen(colony));
                    return true;
                }
                return false;
            }
        });
    }

    private List<String> listSaves() {
        try {
            return Paths.get().listAutosaves();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
