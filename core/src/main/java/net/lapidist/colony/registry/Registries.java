package net.lapidist.colony.registry;

/** Global accessors for type registries. */
public final class Registries {
    private static final TileRegistry TILE_REGISTRY = new TileRegistry();
    private static final BuildingRegistry BUILDING_REGISTRY = new BuildingRegistry();
    private static final ResourceRegistry RESOURCE_REGISTRY = new ResourceRegistry();

    private Registries() { }

    /** @return tile type registry */
    public static TileRegistry tiles() {
        return TILE_REGISTRY;
    }

    /** @return building type registry */
    public static BuildingRegistry buildings() {
        return BUILDING_REGISTRY;
    }

    /** @return resource type registry */
    public static ResourceRegistry resources() {
        return RESOURCE_REGISTRY;
    }
}
