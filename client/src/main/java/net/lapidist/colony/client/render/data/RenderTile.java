package net.lapidist.colony.client.render.data;

/** Render data object for a map tile. */
public final class RenderTile {
    private final int x;
    private final int y;
    private final String tileType;
    private final boolean selected;
    private final int wood;
    private final int stone;
    private final int food;

    private RenderTile(final Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.tileType = builder.tileType;
        this.selected = builder.selected;
        this.wood = builder.wood;
        this.stone = builder.stone;
        this.food = builder.food;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getTileType() {
        return tileType;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getWood() {
        return wood;
    }

    public int getStone() {
        return stone;
    }

    public int getFood() {
        return food;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link RenderTile}. */
    public static final class Builder {
        private int x;
        private int y;
        private String tileType;
        private boolean selected;
        private int wood;
        private int stone;
        private int food;

        private Builder() { }

        public Builder x(final int newX) {
            this.x = newX;
            return this;
        }

        public Builder y(final int newY) {
            this.y = newY;
            return this;
        }

        public Builder tileType(final String newType) {
            this.tileType = newType;
            return this;
        }

        public Builder selected(final boolean newSelected) {
            this.selected = newSelected;
            return this;
        }

        public Builder wood(final int newWood) {
            this.wood = newWood;
            return this;
        }

        public Builder stone(final int newStone) {
            this.stone = newStone;
            return this;
        }

        public Builder food(final int newFood) {
            this.food = newFood;
            return this;
        }

        public RenderTile build() {
            return new RenderTile(this);
        }
    }
}
