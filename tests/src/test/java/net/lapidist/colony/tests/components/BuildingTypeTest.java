package net.lapidist.colony.tests.components;

import com.badlogic.gdx.Gdx;
import net.lapidist.colony.mod.PrototypeManager;
import net.lapidist.colony.i18n.I18n;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class BuildingTypeTest {
    @Test
    public void returnsLocalizedString() {
        PrototypeManager.load(Gdx.files.internal("sample-mod.json"));
        I18n.setLocale(Locale.ENGLISH);
        assertEquals("House", PrototypeManager.building("HOUSE").toString());
        I18n.setLocale(Locale.FRENCH);
        assertEquals("Maison", PrototypeManager.building("HOUSE").toString());
    }
}
