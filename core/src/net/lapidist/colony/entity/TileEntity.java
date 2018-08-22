package net.lapidist.colony.entity;

public abstract class TileEntity extends StaticEntity {

    private float hue;

    /**
     * Constructor for TileEntity.
     */
    public TileEntity(float x, float y, float z, byte angle) {
        super(x, y, z, angle);
    }

    /**
     * @return The hue.
     */
    public float getHue() {
        return hue;
    }

    /**
     * @param hue The hue to set.
     */
    public void setHue(float hue) {
        this.hue = hue;
    }
}
