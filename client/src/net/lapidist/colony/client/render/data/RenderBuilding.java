package net.lapidist.colony.client.render.data;

/** Render data object for a building. */
public final class RenderBuilding {
    private final int x;
    private final int y;
    private final String buildingType;

    private RenderBuilding(final Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.buildingType = builder.buildingType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

        public Builder buildingType(final String newType) {
            this.buildingType = newType;
            return this;
        }

        public RenderBuilding build() {
            return new RenderBuilding(this);
        }
    }
}
