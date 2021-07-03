package net.lapidist.colony.core.io;

import com.badlogic.gdx.utils.Array;

public class ResourceConfig {

    private Array<ResourceTexture> textures;
    private Array<ResourceSound> sounds;

    public static class ResourceTexture {
        private String name;
        private String filePath;
        private Array<ResourceTextureRegion> regions;

        public final String getName() {
            return name;
        }

        public final String getFilePath() {
            return filePath;
        }

        public final Array<ResourceTextureRegion> getTextureRegions() {
            return regions;
        }
    }

    public static class ResourceTextureRegion {
        private String name;
        private String bounds;

        public final String getName() {
            return name;
        }

        public final String getBounds() {
            return bounds;
        }
    }

    public static class ResourceSound {
        private String name;
        private String filePath;

        public final String getName() {
            return name;
        }

        public final String getFilePath() {
            return filePath;
        }
    }

    public final Array<ResourceTexture> getTextures() {
        return textures;
    }

    public final ResourceTexture getTexture(final int index) {
        return textures.get(index);
    }

    public final ResourceTextureRegion getTextureRegion(
            final int imageIndex,
            final int regionIndex
    ) {
        return textures.get(imageIndex).getTextureRegions().get(regionIndex);
    }

    public final Array<ResourceTextureRegion> getTextureRegions(
            final int imageIndex
    ) {
        return textures.get(imageIndex).getTextureRegions();
    }

    public final Array<ResourceSound> getSounds() {
        return sounds;
    }

    public final ResourceSound getSound(final int index) {
        return sounds.get(index);
    }
}
