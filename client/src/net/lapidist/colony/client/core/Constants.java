package net.lapidist.colony.client.core;

public final class Constants {

    private Constants() {
    }

    public static final String NAME = "Colony";
    public static final String VERSION = Version.get();
    public static final boolean DEBUG = true;
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    public static final int TARGET_FPS = 60;
    public static final int TILE_SIZE = net.lapidist.colony.components.GameConstants.TILE_SIZE;
    public static final int MAP_WIDTH = net.lapidist.colony.components.GameConstants.MAP_WIDTH;
    public static final int MAP_HEIGHT = net.lapidist.colony.components.GameConstants.MAP_HEIGHT;

    public static final int MAP_GUTTER = 4;

    private static final class Version {
        private static String get() {
            String v = Constants.class.getPackage().getImplementationVersion();
            return v != null ? v : "dev";
        }
    }
}
