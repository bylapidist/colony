package net.lapidist.colony.core.io;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.IOException;

public class ResourceLoader implements Disposable {

    private boolean disposed;
    private boolean loaded;

    private final Json json = new Json();
    private final FileLocation fileLocation;
    private final String resourceConfigPath;
    private final ObjectMap<String, TextureRegion> textureRegions;
    private final ObjectMap<String, Sound> sounds;

    public ResourceLoader(
            final FileLocation fileLocationToSet,
            final String resourceConfigPathToSet
    ) {
        this.fileLocation = fileLocationToSet;
        this.resourceConfigPath = resourceConfigPathToSet;

        textureRegions = new ObjectMap<>();
        sounds = new ObjectMap<>();
    }

    public final void load() throws RuntimeException, IOException {
        FileHandle resourceConfigHandle
                = fileLocation.getFile(resourceConfigPath);

        if (resourceConfigHandle == null || !resourceConfigHandle.exists()) {
            throw new IOException(
                    String.format("%s does not exist", resourceConfigPath)
            );
        }

        ResourceConfig resourceConfig = json.fromJson(
                ResourceConfig.class, resourceConfigHandle.readString()
        );

        loaded = true;
    }

    public final boolean isLoaded() {
        return loaded;
    }

    @Override
    public final void dispose() {
        if (!disposed) {
            disposed = true;

            for (TextureRegion entry : new ObjectMap.Values<>(textureRegions)) {
                entry.getTexture().dispose();
            }
            textureRegions.clear();

            for (Sound entry : new ObjectMap.Values<>(sounds)) {
                entry.dispose();
            }
            sounds.clear();
        }
    }
}
