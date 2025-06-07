package net.lapidist.colony.client.core.io;

import com.badlogic.gdx.utils.Array;

public final class ResourceConfig {

    private Array<ResourceTexture> textures;
    private Array<ResourceSound> sounds;

    public static final class ResourceTexture {
        private String name;
        private String filePath;
        private Array<ResourceTextureRegion> regions;

        public String getName() {
            return name;
        }

        public String getFilePath() {
            return filePath;
        }

        public Array<ResourceTextureRegion> getTextureRegions() {
            return regions;
        }

        void setName(final String nameToSet) {
            this.name = nameToSet;
        }

        void setFilePath(final String filePathToSet) {
            this.filePath = filePathToSet;
        }

        void setTextureRegions(final Array<ResourceTextureRegion> regionsToSet) {
            this.regions = regionsToSet;
        }
    }

    public static final class ResourceTextureRegion {
        private String name;
        private String bounds;

        public String getName() {
            return name;
        }

        public String getBounds() {
            return bounds;
        }

        void setName(final String nameToSet) {
            this.name = nameToSet;
        }

        void setBounds(final String boundsToSet) {
            this.bounds = boundsToSet;
        }
    }

    public static final class ResourceSound {
        private String name;
        private String filePath;

        public String getName() {
            return name;
        }

        public String getFilePath() {
            return filePath;
        }

        void setName(final String nameToSet) {
            this.name = nameToSet;
        }

        void setFilePath(final String filePathToSet) {
            this.filePath = filePathToSet;
        }
    }

    public Array<ResourceTexture> getTextures() {
        return textures;
    }

    void setTextures(final Array<ResourceTexture> texturesToSet) {
        this.textures = texturesToSet;
    }

    public ResourceTexture getTexture(final int index) {
        return textures.get(index);
    }

    public ResourceTextureRegion getTextureRegion(
            final int imageIndex,
            final int regionIndex
    ) {
        return textures.get(imageIndex).getTextureRegions().get(regionIndex);
    }

    public Array<ResourceTextureRegion> getTextureRegions(
            final int imageIndex
    ) {
        return textures.get(imageIndex).getTextureRegions();
    }

    public Array<ResourceSound> getSounds() {
        return sounds;
    }

    void setSounds(final Array<ResourceSound> soundsToSet) {
        this.sounds = soundsToSet;
    }

    public ResourceSound getSound(final int index) {
        return sounds.get(index);
    }
}
