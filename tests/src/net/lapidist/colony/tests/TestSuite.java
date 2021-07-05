package net.lapidist.colony.tests;

import net.lapidist.colony.tests.core.events.EventsTest;
import net.lapidist.colony.tests.core.io.FileLocationTest;
import net.lapidist.colony.tests.core.io.ResourceLoaderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UnitTestExample.class,
        EventsTest.class,
        FileLocationTest.class,
        ResourceLoaderTest.class
})
public class TestSuite {
}
