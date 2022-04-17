package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import net.lapidist.colony.client.core.Constants;

import java.io.IOException;

public class ResourceLoader implements Disposable {

    private static final int NUM_BOUNDS = 4;
    private static final int BOUND_X = 0;
    private static final int BOUND_Y = 1;
    private static final int BOUND_WIDTH = 2;
    private static final int BOUND_HEIGHT = 3;

    private boolean disposed;
    private boolean loaded;
    private ResourceConfig resourceConfig;

    private final Json json = new Json();
    private FileLocation fileLocation;
    private final ObjectMap<String, TextureRegion> textureRegions;
    private final ObjectMap<String, Sound> sounds;

    public ResourceLoader() {
        textureRegions = new ObjectMap<>();
        sounds = new ObjectMap<>();
    }

    public final void load(
            final FileLocation fileLocationToSet,
            final String resourceConfigPathToSet
    ) throws IOException {
        fileLocation = fileLocationToSet;

        FileHandle resourceConfigHandle
                = fileLocation.getFile(resourceConfigPathToSet);

        if (resourceConfigHandle == null || !resourceConfigHandle.exists()) {
            throw new IOException(
                    String.format("%s does not exist", resourceConfigPathToSet)
            );
        }

        String resourceConfigJson = resourceConfigHandle.readString();

        resourceConfig = json.fromJson(
                ResourceConfig.class, resourceConfigJson
        );

        if (Constants.DEBUG) {
            System.out.printf(
                    "[%s] ResourceConfig loaded from \"%s\"\n%s\n",
                    ResourceLoader.class.getSimpleName(),
                    resourceConfigPathToSet,
                    resourceConfigJson
            );
        }

        loadTextureRegions();

        loaded = true;
    }

    private void loadTextureRegions() throws IOException {
        for (
                ResourceConfig.ResourceTexture image
                    : new Array.ArrayIterator<>(resourceConfig.getTextures())
        ) {
            FileHandle textureFileHandle
                    = fileLocation.getFile(image.getFilePath());

            if (textureFileHandle == null || !textureFileHandle.exists()) {
                throw new IOException(
                        String.format("%s does not exist", image.getFilePath())
                );
            }

            Texture texture = new Texture(textureFileHandle);
            texture.setFilter(
                    Texture.TextureFilter.Linear,
                    Texture.TextureFilter.Linear
            );

            for (
                    ResourceConfig.ResourceTextureRegion region
                        : new Array.ArrayIterator<>(image.getTextureRegions())
            ) {
                textureRegions.put(
                        region.getName(),
                        getTextureRegionFromBounds(texture, region.getBounds())
                );

                if (Constants.DEBUG) {
                    System.out.printf(
                            "[%s] (%s:%s:%s) TextureRegion loaded from \"%s\"\n",
                            ResourceLoader.class.getSimpleName(),
                            image.getName(),
                            region.getName(),
                            region.getBounds(),
                            image.getFilePath()
                    );
                }
            }
        }
    }

    private TextureRegion getTextureRegionFromBounds(
            final Texture texture,
            final String bounds
    ) {
        String[] boundArray = bounds.split("\\s");
        if (boundArray.length != NUM_BOUNDS) {
            throw new RuntimeException("Need x, y, width and height");
        }

        return new TextureRegion(
                texture,
                Integer.parseInt(boundArray[BOUND_X]),
                Integer.parseInt(boundArray[BOUND_Y]),
                Integer.parseInt(boundArray[BOUND_WIDTH]),
                Integer.parseInt(boundArray[BOUND_HEIGHT])
        );
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
