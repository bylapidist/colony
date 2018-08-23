package net.lapidist.colony;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.PooledEngine;
import net.lapidist.colony.core.*;
import net.lapidist.colony.io.ResourceLoader;

public class Constants {
    public static final String NAME = "Colony";
    public static final String VERSION = "1.0.0";
    public static final boolean DEBUG = true;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1280;
    public static final float PPM = 147.5f; // Pixels per meter

    public static GameState state;
    public static ResourceLoader resourceLoader;
    public static TweenManager tweenManager;
    public static PooledEngine engine;
    public static Logic logic;
    public static World world;
    public static Control control;
    public static UI ui;
    public static Renderer renderer;
}
