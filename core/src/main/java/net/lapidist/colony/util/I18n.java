package net.lapidist.colony.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Simple internationalisation utility backed by {@link ResourceBundle}.
 */
public final class I18n {
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault());

    private I18n() {
    }

    /**
     * Get the translated string for the given key.
     *
     * @param key message key
     * @return translated string or {@code !key!} if missing
     */
    public static String get(final String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * Change the locale used for translations.
     *
     * @param locale new locale
     */
    public static void setLocale(final Locale locale) {
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    /**
     * Get the current locale.
     *
     * @return active locale
     */
    public static Locale getLocale() {
        return bundle.getLocale();
    }
}
