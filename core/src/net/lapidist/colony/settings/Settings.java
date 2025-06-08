package net.lapidist.colony.settings;

import net.lapidist.colony.io.Paths;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

/**
 * Simple POJO for user settings persisted to the game folder.
 */
public final class Settings {
    private static final String LANGUAGE_KEY = "language";

    private Locale locale = Locale.getDefault();

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale localeToSet) {
        this.locale = localeToSet;
    }

    /**
     * Load settings from disk. If the file does not exist default settings are returned.
     */
    public static Settings load() {
        Settings settings = new Settings();
        try {
            Path file = Paths.getSettingsFile();
            if (Files.exists(file)) {
                Properties props = new Properties();
                try (InputStream in = Files.newInputStream(file)) {
                    props.load(in);
                }
                String lang = props.getProperty(LANGUAGE_KEY);
                if (lang != null && !lang.isEmpty()) {
                    settings.setLocale(Locale.forLanguageTag(lang));
                }
            }
        } catch (IOException e) {
            // ignore and use defaults
        }
        return settings;
    }

    /**
     * Save settings to disk.
     */
    public void save() throws IOException {
        Properties props = new Properties();
        props.setProperty(LANGUAGE_KEY, locale.toLanguageTag());
        Path file = Paths.getSettingsFile();
        try (OutputStream out = Files.newOutputStream(file)) {
            props.store(out, null);
        }
    }
}
