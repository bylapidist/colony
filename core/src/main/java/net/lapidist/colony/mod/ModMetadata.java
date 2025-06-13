package net.lapidist.colony.mod;

import java.util.List;

/**
 * Metadata describing a game mod as loaded from {@code mod.json}.
 *
 * @param id           unique identifier
 * @param version      version string
 * @param dependencies list of required mod ids
 */
public record ModMetadata(String id, String version, List<String> dependencies) {
}
