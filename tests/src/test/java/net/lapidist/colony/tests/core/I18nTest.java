package net.lapidist.colony.tests.core;

import net.lapidist.colony.i18n.I18n;
import org.junit.Test;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/** Basic tests for the i18n utility. */
public class I18nTest {

    @Test
    public void returnsTranslationFromBundle() {
        I18n.setLocale(Locale.ENGLISH);
        assertEquals("Continue", I18n.get("main.continue"));
    }

    @Test
    public void returnsTranslationForOtherLocale() {
        I18n.setLocale(Locale.GERMAN);
        assertEquals("Fortsetzen", I18n.get("main.continue"));
    }

    @Test
    public void returnsPlaceholderForMissingKey() {
        I18n.setLocale(Locale.ENGLISH);
        assertEquals("!missing!", I18n.get("missing"));
    }

    @Test
    public void getLocaleReflectsCurrentLocale() {
        I18n.setLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH.getLanguage(), I18n.getLocale().getLanguage());
    }
}
