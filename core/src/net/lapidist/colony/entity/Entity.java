package net.lapidist.colony.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.Colony;
import net.lapidist.colony.map.Map;

public abstract class Entity {

    public static final TextureAtlas textures = new TextureAtlas(Gdx.files.internal("entity/entities.atlas"));

    private short id;
    private float x, y, z;
    private byte angle;
    private String type;
    private Sprite sprite;
    private int zindex;
    private boolean logical;
    private boolean solid;
    private boolean visible;
    private boolean idle;

    /**
     * Constructor for Entity.
     */
    public Entity(float x, float y, float z, byte angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }

    /**
     * Update entity.
     */
    public abstract void update();

    /**
     * Render entity.
     * @param delta Delta.
     */
    public void render(float delta) {
        if (isVisible()) {
            Vector2 sc = Map.toScreenCoords(getX(), getY());
            getSprite().setPosition(sc.x, sc.y + (Map.TILE_HEIGHT_DIAMOND * getZ()));
            getSprite().draw(Colony.game.batch);
        }
    }

    /**
     * @return The x.
     */
    public float getX() {
        return x;
    }

    /**
     * @param x The x to set.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return The y.
     */
    public float getY() {
        return y;
    }

    /**
     * @param y The y to set.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return The z.
     */
    public float getZ() {
        return z;
    }

    /**
     * @param z The z to set.
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * @param x The x to set.
     * @param y The y to set.
     * @param z The z to set.
     */
    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return The angle.
     */
    public byte getAngle() {
        return angle;
    }

    /**
     * @param angle The angle to set.
     */
    public void setAngle(byte angle) {
        this.angle = angle;
    }

    /**
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
        setSprite(Entity.textures.createSprite(type + angle));
    }

    /**
     * @return The id.
     */
    public short getId() {
        return id;
    }

    /**
     * @return The logical.
     */
    public boolean isLogical() {
        return logical;
    }

    /**
     * @param logical The logical to set.
     */
    public void setLogical(boolean logical) {
        this.logical = logical;
    }

    /**
     * @return The solid.
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     * @param solid The solid to set.
     */
    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    /**
     * @return The visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible The visible to set.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return The idle.
     */
    public boolean isIdle() {
        return idle;
    }

    /**
     * @param idle The idle to set.
     */
    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    /**
     * @return The sprite.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * @param sprite The sprite to set.
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * @return The zindex.
     */
    public int getZIndex() {
        return zindex;
    }

    /**
     * @param zindex The index to set.
     */
    public void setZIndex(int zindex) {
        this.zindex = zindex;
    }

}
