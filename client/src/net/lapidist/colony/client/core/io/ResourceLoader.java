package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import net.lapidist.colony.client.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ResourceLoader implements Disposable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);

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
    private AssetManager assetManager;
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

        FileHandle resourceConfigHandle =
                fileLocation.getFile(resourceConfigPathToSet);

        if (resourceConfigHandle == null || !resourceConfigHandle.exists()) {
            throw new IOException(
                    String.format("%s does not exist", resourceConfigPathToSet)
            );
        }

        String resourceConfigString = resourceConfigHandle.readString();

        com.typesafe.config.Config config =
                com.typesafe.config.ConfigFactory.parseString(resourceConfigString);
        resourceConfig = parseConfig(config);

        if (Constants.DEBUG) {
            LOGGER.debug(
                    "ResourceConfig loaded from \"{}\"\n{}",
                    resourceConfigPathToSet,
                    resourceConfigString
            );
        }

        assetManager = new AssetManager(fileLocation.getResolver());

        loadAssets();
        assetManager.finishLoading();

        loadTextureRegions();

        loaded = true;
    }

    private void loadAssets() {
        if (resourceConfig.getTextures() != null) {
            for (ResourceConfig.ResourceTexture texture : new Array.ArrayIterator<>(resourceConfig.getTextures())) {
                assetManager.load(texture.getFilePath(), Texture.class);
            }
        }

        if (resourceConfig.getSounds() != null) {
            for (ResourceConfig.ResourceSound sound : new Array.ArrayIterator<>(resourceConfig.getSounds())) {
                assetManager.load(sound.getFilePath(), Sound.class);
            }
        }
    }

    private void loadTextureRegions() {
        if (resourceConfig.getTextures() == null) {
            return;
        }

        for (ResourceConfig.ResourceTexture image
                : new Array.ArrayIterator<>(resourceConfig.getTextures())) {
            Texture texture = assetManager.get(image.getFilePath(), Texture.class);
            texture.setFilter(
                    Texture.TextureFilter.Linear,
                    Texture.TextureFilter.Linear
            );

            for (ResourceConfig.ResourceTextureRegion region
                    : new Array.ArrayIterator<>(image.getTextureRegions())) {
                textureRegions.put(
                        region.getName(),
                        getTextureRegionFromBounds(texture, region.getBounds())
                );

                if (Constants.DEBUG) {
                    LOGGER.debug(
                            "({}:{}:{}) TextureRegion loaded from \"{}\"",
                            image.getName(),
                            region.getName(),
                            region.getBounds(),
                            image.getFilePath()
                    );
                }
            }
        }

        if (resourceConfig.getSounds() != null) {
            for (ResourceConfig.ResourceSound sound
                    : new Array.ArrayIterator<>(resourceConfig.getSounds())) {
                Sound s = assetManager.get(sound.getFilePath(), Sound.class);
                sounds.put(sound.getName(), s);
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

            textureRegions.clear();
            sounds.clear();

            if (assetManager != null) {
                assetManager.dispose();
            }
        }
    }

    public final boolean isDisposed() {
        return disposed;
    }

    public final ResourceConfig getResourceConfig() {
        return resourceConfig;
    }

    public final Json getJson() {
        return json;
    }

    public final FileLocation getFileLocation() {
        return fileLocation;
    }

    public final ObjectMap<String, TextureRegion> getTextureRegions() {
        return textureRegions;
    }

    public final ObjectMap<String, Sound> getSounds() {
        return sounds;
    }

    private ResourceConfig parseConfig(final com.typesafe.config.Config config) {
        ResourceConfig rc = new ResourceConfig();

        com.badlogic.gdx.utils.Array<ResourceConfig.ResourceTexture> textures =
                new com.badlogic.gdx.utils.Array<>();
        if (config.hasPath("textures")) {
            for (com.typesafe.config.Config texConf : config.getConfigList("textures")) {
                ResourceConfig.ResourceTexture texture = new ResourceConfig.ResourceTexture();
                texture.setName(texConf.getString("name"));
                texture.setFilePath(texConf.getString("filePath"));
                com.badlogic.gdx.utils.Array<ResourceConfig.ResourceTextureRegion> regions =
                        new com.badlogic.gdx.utils.Array<>();
                if (texConf.hasPath("regions")) {
                    for (com.typesafe.config.Config regionConf : texConf.getConfigList("regions")) {
                        ResourceConfig.ResourceTextureRegion region = new ResourceConfig.ResourceTextureRegion();
                        region.setName(regionConf.getString("name"));
                        region.setBounds(regionConf.getString("bounds"));
                        regions.add(region);
                    }
                }
                texture.setTextureRegions(regions);
                textures.add(texture);
            }
        }
        rc.setTextures(textures);

        com.badlogic.gdx.utils.Array<ResourceConfig.ResourceSound> soundsArray = new com.badlogic.gdx.utils.Array<>();
        if (config.hasPath("sounds")) {
            for (com.typesafe.config.Config soundConf : config.getConfigList("sounds")) {
                ResourceConfig.ResourceSound sound = new ResourceConfig.ResourceSound();
                sound.setName(soundConf.getString("name"));
                sound.setFilePath(soundConf.getString("filePath"));
                soundsArray.add(sound);
            }
        }
        rc.setSounds(soundsArray);
        return rc;
    }
}
