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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.util.I18n;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.io.Paths;
import net.lapidist.colony.save.io.GameStateIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LoadGameScreen extends BaseScreen {
    private static final float PADDING = 10f;
    private final Colony colony;
    private final List<String> saves;
    private final Table list;
    private final TextField filterField;

    public LoadGameScreen(final Colony game) {
        this.colony = game;
        float scale = game.getSettings() == null ? 1f : game.getSettings().getUiScale();
        getStage().getRoot().setScale(scale);

        this.saves = listSaves();
        this.list = new Table();
        this.filterField = new TextField("", getSkin());
        filterField.setMessageText(I18n.get("loadGame.filterPlaceholder"));
        filterField.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                populateList(filterField.getText());
            }
        });

        populateList("");

        ScrollPane scroll = new ScrollPane(list, getSkin());
        scroll.setScrollingDisabled(true, false);
        getRoot().add(filterField).growX().row();
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

    private void populateList(final String filter) {
        list.clearChildren();
        String lower = filter.toLowerCase();
        java.util.List<String> matched = new java.util.ArrayList<>();
        for (String save : saves) {
            if (save.toLowerCase().contains(lower)) {
                matched.add(save);
            }
        }
        for (String save : matched) {
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

        if (matched.isEmpty()) {
            list.add(new Label(I18n.get("loadGame.none"), getSkin())).row();
        }
    }

    private List<String> listSaves() {
        List<String> names;
        try {
            names = Paths.get().listAutosaves();
        } catch (IOException e) {
            return new ArrayList<>();
        }

        try {
            // sort by most recently modified first
            names.sort((a, b) -> {
                try {
                    java.nio.file.Path pa = Paths.get().getAutosave(a);
                    java.nio.file.Path pb = Paths.get().getAutosave(b);
                    java.nio.file.attribute.FileTime ta = java.nio.file.Files.getLastModifiedTime(pa);
                    java.nio.file.attribute.FileTime tb = java.nio.file.Files.getLastModifiedTime(pb);
                    return -ta.compareTo(tb);
                } catch (IOException ex) {
                    // propagate to outer catch block
                    throw new RuntimeException(ex);
                }
            });
        } catch (RuntimeException e) {
            names.sort(String::compareTo);
        }

        return names;
    }

}
