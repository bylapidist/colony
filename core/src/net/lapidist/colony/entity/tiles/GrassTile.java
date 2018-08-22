package net.lapidist.colony.entity.tiles;

import net.lapidist.colony.entity.TileEntity;

public class GrassTile extends TileEntity {

    /**
     * Constructor for GrassTile.
     * @param x x coord.
     * @param y y coord.
     * @param z z coord.
     * @param angle Angle.
     */
    public GrassTile(float x, float y, float z, byte angle) {
        super(x, y, z, angle);

        this.setType("Grass");
        this.setIdle(true);
        this.setVisible(true);
        this.setZIndex(0);
    }

    @Override
    public void update() {
        if (!isIdle() && isLogical()) {
            // Update logic.
        }
    }
}
