package net.lapidist.colony.mod;

import net.lapidist.colony.serialization.KryoType;
import java.util.List;

/**
 * Metadata describing a game mod as loaded from {@code mod.json}.
 *
 * @param id           unique identifier
 * @param version      version string
 * @param dependencies list of required mod ids
 */
@KryoType
public record ModMetadata(String id, String version, List<String> dependencies) {
}
