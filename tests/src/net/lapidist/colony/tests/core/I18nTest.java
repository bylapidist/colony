package net.lapidist.colony.tests.core;

import net.lapidist.colony.i18n.I18n;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/** Basic tests for the i18n utility. */
public class I18nTest {

    @Test
    public void returnsTranslationFromBundle() {
        assertEquals("Continue", I18n.get("main.continue"));
    }
}
