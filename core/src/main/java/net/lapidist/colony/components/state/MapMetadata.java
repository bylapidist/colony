package net.lapidist.colony.components.state;

import net.lapidist.colony.serialization.KryoType;

import java.util.List;

/**
 * Map information sent prior to chunk data.
 */
@KryoType
public record MapMetadata(
        int version,
        String name,
        String saveName,
        String autosaveName,
        String description,
        List<BuildingData> buildings,
        ResourceData playerResources,
        PlayerPosition playerPos,
        CameraPosition cameraPos,
        EnvironmentState environment,
        int width,
        int height,
        int chunkCount
) { }
