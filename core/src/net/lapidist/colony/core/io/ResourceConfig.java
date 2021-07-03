package net.lapidist.colony.core.io;

import com.badlogic.gdx.utils.Array;

public class ResourceConfig {

    private Array<ResourceImage> images;
    private Array<ResourceSound> sounds;

    public static class ResourceImage {
        private String name;
        private String filePath;
        private Array<ResourceImageRegion> regions;

        public final String getName() {
            return name;
        }

        public final String getFilePath() {
            return filePath;
        }

        public final Array<ResourceImageRegion> getRegions() {
            return regions;
        }
    }

    public static class ResourceImageRegion {
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

    public final Array<ResourceImage> getImages() {
        return images;
    }

    public final ResourceImage getImage(final int index) {
        return images.get(index);
    }

    public final ResourceImageRegion getImageRegion(
            final int imageIndex,
            final int regionIndex
    ) {
        return images.get(imageIndex).getRegions().get(regionIndex);
    }

    public final Array<ResourceImageRegion> getImageRegions(
            final int imageIndex
    ) {
        return images.get(imageIndex).getRegions();
    }

    public final Array<ResourceSound> getSounds() {
        return sounds;
    }

    public final ResourceSound getSound(final int index) {
        return sounds.get(index);
    }
}
