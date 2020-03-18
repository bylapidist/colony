package net.lapidist.colony.core;

public final class Constants {

    private Constants() {
    }

    public static final String NAME = "Colony";
    public static final String VERSION = "1.0.0";
    public static final boolean DEBUG = true;
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    public static final int PPM = 32; // Pixels per meter
    public static final int CHUNK_SIZE = 32;
    public static final int MAP_SIZE = 32;
    public static final int TARGET_FPS = 60;
    public static final int DEFAULT_PORT = 9966;
    public static final String DEFAULT_HOSTNAME = "127.0.0.1";
    public static final int MAX_PACKET_SIZE = 10_000;
}
