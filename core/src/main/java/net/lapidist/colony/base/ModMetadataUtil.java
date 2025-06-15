package net.lapidist.colony.base;

import java.util.Map;
import java.util.Optional;

import net.lapidist.colony.mod.GameMod;
import net.lapidist.colony.mod.ModMetadata;

/** Utility to build metadata for built-in mods. */
public final class ModMetadataUtil {
    private static final Map<String, String> IDS = Map.ofEntries(
            Map.entry("net.lapidist.colony.base.BaseMapServiceMod", "base-map-service"),
            Map.entry("net.lapidist.colony.base.BaseNetworkMod", "base-network"),
            Map.entry("net.lapidist.colony.base.BaseAutosaveMod", "base-autosave"),
            Map.entry("net.lapidist.colony.base.BaseResourceProductionMod", "base-resource-production"),
            Map.entry("net.lapidist.colony.base.BaseHandlersMod", "base-handlers"),
            Map.entry("net.lapidist.colony.base.BaseDefinitionsMod", "base-definitions"),
            Map.entry("net.lapidist.colony.base.BaseResourcesMod", "base-resources"),
            Map.entry("net.lapidist.colony.base.BaseItemsMod", "base-items"),
            Map.entry("net.lapidist.colony.base.BaseCommandBusMod", "base-command-bus"),
            Map.entry("net.lapidist.colony.base.BaseGameplaySystemsMod", "base-systems")
    );

    private ModMetadataUtil() {
    }

    /**
     * Create metadata for a built-in mod class.
     *
     * @param cls mod class
     * @return metadata with inferred id and version
     */
    public static ModMetadata builtinMetadata(final Class<? extends GameMod> cls) {
        String id = IDS.getOrDefault(cls.getName(), cls.getSimpleName());
        String version = Optional.ofNullable(cls.getPackage().getImplementationVersion())
                .orElse("dev");
        return new ModMetadata(id, version, new java.util.ArrayList<>());
    }
}
