package net.lapidist.colony.tests.components;

import net.lapidist.colony.util.I18n;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class BuildingTypeTest {
    @Test
    public void returnsLocalizedString() {
        I18n.setLocale(Locale.ENGLISH);
        assertEquals("House", I18n.get("building.house"));
        I18n.setLocale(Locale.FRENCH);
        assertEquals("Maison", I18n.get("building.house"));
    }
}
