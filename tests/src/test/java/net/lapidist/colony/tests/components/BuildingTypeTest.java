package net.lapidist.colony.tests.components;

import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.i18n.I18n;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class BuildingTypeTest {
    @Test
    public void returnsLocalizedString() {
        I18n.setLocale(Locale.ENGLISH);
        assertEquals("House", BuildingComponent.BuildingType.HOUSE.toString());
        I18n.setLocale(Locale.FRENCH);
        assertEquals("Maison", BuildingComponent.BuildingType.HOUSE.toString());
    }

    @Test
    public void enumContainsFarm() {
        assertEquals(
                BuildingComponent.BuildingType.FARM,
                BuildingComponent.BuildingType.valueOf("FARM")
        );
    }
}
