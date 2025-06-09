package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.i18n.I18n;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.save.SaveVersion;
import net.lapidist.colony.server.io.GameStateIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LoadGameScreen extends BaseScreen {
    private static final float PADDING = 10f;
    private final Colony colony;

    public LoadGameScreen(final Colony game) {
        this.colony = game;

        List<String> saves = listSaves();
        for (String save : saves) {
            int version;
            try {
                version = GameStateIO.readVersion(Paths.getAutosave(save));
            } catch (IOException e) {
                version = -1;
            }

            String text = save + " (v" + version + ")";
            TextButton loadButton = new TextButton(text, getSkin());
            TextButton migrateButton = new TextButton(I18n.get("loadGame.migrate"), getSkin());
            TextButton deleteButton = new TextButton(I18n.get("loadGame.delete"), getSkin());
            Table row = new Table();
            row.add(loadButton).padRight(PADDING);
            if (version < SaveVersion.CURRENT.number()) {
                loadButton.setDisabled(true);
                row.add(migrateButton).padRight(PADDING);
            }
            row.add(deleteButton);
            getRoot().add(row).row();

            loadButton.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    colony.startGame(save);
                }
            });

            migrateButton.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    colony.startGame(save);
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
                                    Paths.deleteAutosave(save);
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

        TextButton backButton = new TextButton(I18n.get("common.back"), getSkin());
        getRoot().add(backButton).row();
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
            }
        });
    }

    private List<String> listSaves() {
        try {
            return Paths.listAutosaves();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
