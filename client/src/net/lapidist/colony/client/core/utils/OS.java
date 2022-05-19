package net.lapidist.colony.client.core.utils;

import java.util.Locale;

public final class OS {

    private static final String OPERATING_SYSTEM = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

    private OS() {
    }

    public static Boolean isMac() {
        return OPERATING_SYSTEM.contains("mac") || OPERATING_SYSTEM.contains("darwin");
    }

    public static Boolean isWindows() {
        return OPERATING_SYSTEM.contains("windows");
    }

    public static String getOperatingSystem() {
        return OPERATING_SYSTEM;
    }
}
