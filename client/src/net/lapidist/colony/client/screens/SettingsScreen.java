package net.lapidist.colony.client.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
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

    public SettingsScreen(final Colony game) {
        this.colony = game;

        TextButton en = new TextButton(I18n.get("language.en"), getSkin());
        TextButton fr = new TextButton(I18n.get("language.fr"), getSkin());
        TextButton es = new TextButton(I18n.get("language.es"), getSkin());
        TextButton de = new TextButton(I18n.get("language.de"), getSkin());
        TextButton back = new TextButton(I18n.get("common.back"), getSkin());

        getRoot().add(en).row();
        getRoot().add(fr).row();
        getRoot().add(es).row();
        getRoot().add(de).row();
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
        back.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                colony.setScreen(new MainMenuScreen(colony));
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
    }
}
