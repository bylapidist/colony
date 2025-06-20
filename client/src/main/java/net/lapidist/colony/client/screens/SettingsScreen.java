package net.lapidist.colony.client.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.lapidist.colony.client.Colony;
import net.lapidist.colony.util.I18n;

import java.io.IOException;
import java.util.Locale;

/**
 * Simple settings screen allowing the user to change language.
 */
public final class SettingsScreen extends BaseScreen {
    private final Colony colony;
    private static final Locale[] SUPPORTED_LOCALES = new Locale[] {
            Locale.ENGLISH,
            Locale.FRENCH,
            new Locale("es"),
            Locale.GERMAN
    };
    private int localeIndex;
    private final TextButton language;

    public SettingsScreen(final Colony game) {
        this.colony = game;
        float scale = game.getSettings() == null ? 1f : game.getSettings().getUiScale();
        getStage().getRoot().setScale(scale);
        localeIndex = findLocaleIndex(colony.getSettings().getLocale());
        language = new TextButton(getLanguageText(SUPPORTED_LOCALES[localeIndex]), getSkin());
        TextButton keybinds = new TextButton(I18n.get("settings.keybinds"), getSkin());
        TextButton graphics = new TextButton(I18n.get("settings.graphics"), getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());

        getRoot().add(language).row();
        getRoot().add(keybinds).row();
        getRoot().add(graphics).row();
        getRoot().add(back).row();

        language.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                localeIndex = (localeIndex + 1) % SUPPORTED_LOCALES.length;
                Locale next = SUPPORTED_LOCALES[localeIndex];
                switchLocale(next);
                language.setText(getLanguageText(next));
            }
        });
        keybinds.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new KeybindsScreen(colony));
            }
        });
        graphics.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new GraphicsSettingsScreen(colony));
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
                if (keycode == Input.Keys.ESCAPE) {
                    colony.setScreen(new MainMenuScreen(colony));
                    return true;
                }
                return false;
            }
        });
    }

    private static int findLocaleIndex(final Locale locale) {
        for (int i = 0; i < SUPPORTED_LOCALES.length; i++) {
            if (SUPPORTED_LOCALES[i].getLanguage().equals(locale.getLanguage())) {
                return i;
            }
        }
        return 0;
    }

    private static String getLanguageText(final Locale locale) {
        return I18n.get("language." + locale.getLanguage());
    }

    private void switchLocale(final Locale locale) {
        I18n.setLocale(locale);
        colony.getSettings().setLocale(locale);
        try {
            colony.getSettings().save();
        } catch (IOException e) {
            // ignore
        }
    }
}
