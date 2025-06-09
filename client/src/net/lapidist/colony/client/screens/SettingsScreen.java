package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.i18n.I18n;

import java.io.IOException;
import java.util.Locale;

/**
 * Simple settings screen allowing the user to change language.
 */
public final class SettingsScreen extends BaseScreen {
    private final Colony colony;
    private TextButton up;
    private TextButton down;
    private TextButton left;
    private TextButton right;
    private TextButton gather;
    private TextButton awaiting;

    public SettingsScreen(final Colony game) {
        this.colony = game;

        TextButton en = new TextButton(I18n.get("language.en"), getSkin());
        TextButton fr = new TextButton(I18n.get("language.fr"), getSkin());
        TextButton es = new TextButton(I18n.get("language.es"), getSkin());
        TextButton de = new TextButton(I18n.get("language.de"), getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());
        up = new TextButton("", getSkin());
        down = new TextButton("", getSkin());
        left = new TextButton("", getSkin());
        right = new TextButton("", getSkin());
        gather = new TextButton("", getSkin());
        TextButton reset = new TextButton(I18n.get("settings.resetKeybinds"), getSkin());

        getRoot().add(en).row();
        getRoot().add(fr).row();
        getRoot().add(es).row();
        getRoot().add(de).row();
        updateKeyLabels();
        getRoot().add(up).row();
        getRoot().add(down).row();
        getRoot().add(left).row();
        getRoot().add(right).row();
        getRoot().add(gather).row();
        getRoot().add(reset).row();
        getRoot().add(back).row();

        en.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                switchLocale(Locale.ENGLISH);
            }
        });
        fr.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                switchLocale(Locale.FRENCH);
            }
        });
        es.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                switchLocale(new Locale("es"));
            }
        });
        de.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                switchLocale(Locale.GERMAN);
            }
        });
        up.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                awaiting = up;
                up.setText(I18n.get("settings.pressKey"));
            }
        });
        down.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                awaiting = down;
                down.setText(I18n.get("settings.pressKey"));
            }
        });
        left.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                awaiting = left;
                left.setText(I18n.get("settings.pressKey"));
            }
        });
        right.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                awaiting = right;
                right.setText(I18n.get("settings.pressKey"));
            }
        });
        gather.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                awaiting = gather;
                gather.setText(I18n.get("settings.pressKey"));
            }
        });
        reset.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.getSettings().resetKeybinds();
                save();
                updateKeyLabels();
            }
        });
        back.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
            }
        });

        getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (awaiting == null) {
                    return false;
                }
                if (awaiting == up) {
                    colony.getSettings().setUpKey(keycode);
                } else if (awaiting == down) {
                    colony.getSettings().setDownKey(keycode);
                } else if (awaiting == left) {
                    colony.getSettings().setLeftKey(keycode);
                } else if (awaiting == right) {
                    colony.getSettings().setRightKey(keycode);
                } else if (awaiting == gather) {
                    colony.getSettings().setGatherKey(keycode);
                }
                save();
                awaiting = null;
                updateKeyLabels();
                return true;
            }
        });
    }

    private void switchLocale(final Locale locale) {
        I18n.setLocale(locale);
        colony.getSettings().setLocale(locale);
        try {
            colony.getSettings().save();
        } catch (IOException e) {
            // ignore
        }
        updateKeyLabels();
    }

    private void updateKeyLabels() {
        up.setText(I18n.get("settings.key.up") + ": " + Input.Keys.toString(colony.getSettings().getUpKey()));
        down.setText(I18n.get("settings.key.down") + ": " + Input.Keys.toString(colony.getSettings().getDownKey()));
        left.setText(I18n.get("settings.key.left") + ": " + Input.Keys.toString(colony.getSettings().getLeftKey()));
        right.setText(I18n.get("settings.key.right") + ": "
                + Input.Keys.toString(colony.getSettings().getRightKey()));
        gather.setText(I18n.get("settings.key.gather") + ": "
                + Input.Keys.toString(colony.getSettings().getGatherKey()));
    }

    private void save() {
        try {
            colony.getSettings().save();
        } catch (IOException e) {
            // ignore
        }
    }
}
