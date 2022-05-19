package net.lapidist.colony.client.core.serialization;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.utils.PathUtils;
import net.lapidist.colony.components.maps.MapComponent;

import java.io.IOException;

public final class MapSerializer {

    private String mapData;

    public void serialize(final MapComponent map) {
        Json json = new Json();
        mapData  = json.toJson(map);
    }

    public void save(final String slot) throws IOException {
        FileHandle file = FileLocation.ABSOLUTE.getFile(PathUtils.getSaveFolder() + "\\" + slot);
        file.writeString(mapData, false);
    }

    public MapComponent load(final String slot) throws IOException {
        Json json = new Json();
        String loadedMapData = FileLocation.ABSOLUTE.getFile(PathUtils.getSaveFolder() + "\\" + slot).readString();
        return json.fromJson(MapComponent.class, loadedMapData);
    }
}
