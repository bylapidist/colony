package net.lapidist.colony.mod;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import net.lapidist.colony.components.entities.BuildingType;
import net.lapidist.colony.components.resources.ResourceType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** Loads building and resource prototypes from a JSON descriptor. */
public final class PrototypeManager {
    private static final Map<String, BuildingType> BUILDINGS = new HashMap<>();
    private static final Map<String, ResourceType> RESOURCES = new HashMap<>();

    private PrototypeManager() { }

    public static void load(final FileHandle file) {
        JsonValue root = new JsonReader().parse(file);
        BUILDINGS.clear();
        RESOURCES.clear();
        if (root.has("buildings")) {
            for (JsonValue val : root.get("buildings")) {
                BuildingType type = new BuildingType(val.getString("id"), val.getString("key"));
                BUILDINGS.put(type.id(), type);
            }
        }
        if (root.has("resources")) {
            for (JsonValue val : root.get("resources")) {
                ResourceType type = new ResourceType(val.getString("id"));
                RESOURCES.put(type.id(), type);
            }
        }
    }

    public static BuildingType building(final String id) {
        return BUILDINGS.get(id);
    }

    public static ResourceType resource(final String id) {
        return RESOURCES.get(id);
    }

    public static Collection<BuildingType> buildings() {
        return BUILDINGS.values();
    }
}
