package net.lapidist.colony.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class ColonyConfig {

    private static final Config CONFIG = ConfigFactory.load("game.conf");

    private ColonyConfig() {
    }

    public static Config get() {
        return CONFIG;
    }
}
