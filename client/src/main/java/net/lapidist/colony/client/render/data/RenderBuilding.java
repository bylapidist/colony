package net.lapidist.colony.client.render.data;

/** Render data object for a building. */
public final class RenderBuilding {
    private final int x;
    private final int y;
    private final float worldX;
    private final float worldY;
    private final String buildingType;

    private RenderBuilding(final Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.worldX = builder.worldX;
        this.worldY = builder.worldY;
        this.buildingType = builder.buildingType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link RenderBuilding}. */
    public static final class Builder {
        private int x;
        private int y;
        private float worldX;
        private float worldY;
        private String buildingType;

        private Builder() { }

        public Builder x(final int newX) {
            this.x = newX;
            return this;
        }

        public Builder y(final int newY) {
            this.y = newY;
            return this;
        }

        public Builder worldX(final float newWorldX) {
            this.worldX = newWorldX;
            return this;
        }

        public Builder worldY(final float newWorldY) {
            this.worldY = newWorldY;
            return this;
        }

        public Builder buildingType(final String newType) {
            this.buildingType = newType;
            return this;
        }

        public RenderBuilding build() {
            return new RenderBuilding(this);
        }
    }
}
