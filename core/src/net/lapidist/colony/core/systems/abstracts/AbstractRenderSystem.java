package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import net.lapidist.colony.components.*;
import net.lapidist.colony.core.Constants;

public abstract class AbstractRenderSystem extends EntityProcessingSystem {

    private static final int SPRITEBATCH_SIZE = 2000;
    private static final float ONE_EIGHTY_DEGREES = 180f;

    private SpriteBatch batch;
    private TextureRegion tmpTextureRegion;

    public AbstractRenderSystem(final Aspect.Builder aspect) {
        super(aspect);

        tmpTextureRegion = new TextureRegion();
        batch = new SpriteBatch(SPRITEBATCH_SIZE);
    }

    protected abstract void onResize(int width, int height);

    protected abstract void onInit();

    protected final void drawTexture(
            final TextureComponent textureC,
            final RotationComponent angleC,
            final OriginComponent originC,
            final WorldPositionComponent posC,
            final ScaleComponent scaleC,
            final float zoom
    ) {
        float ox = textureC.getTexture().getWidth()
                * scaleC.getScale() * originC.getOrigin().x;
        float oy = textureC.getTexture().getHeight()
                * scaleC.getScale() * originC.getOrigin().y;

        tmpTextureRegion.setTexture(textureC.getTexture());
        tmpTextureRegion.setRegionWidth(textureC.getTexture().getWidth());
        tmpTextureRegion.setRegionHeight(textureC.getTexture().getHeight());

        if (angleC.getRotation() != 0) {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.getPosition().x, zoom),
                    roundToPixels(posC.getPosition().y, zoom),
                    ox,
                    oy,
                    tmpTextureRegion.getRegionWidth()
                            * scaleC.getScale(),
                    tmpTextureRegion.getRegionHeight()
                            * scaleC.getScale(),
                    1,
                    1,
                    angleC.getRotation()
                            * ONE_EIGHTY_DEGREES / MathUtils.PI
            );
        } else {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.getPosition().x, zoom),
                    roundToPixels(posC.getPosition().y, zoom),
                    tmpTextureRegion.getRegionWidth() * scaleC.getScale(),
                    tmpTextureRegion.getRegionHeight() * scaleC.getScale()
            );
        }
    }

    protected final void drawLabel(
            final LabelComponent labelC,
            final FontComponent fontC,
            final WorldPositionComponent posC
    ) {
        fontC.getFont().draw(
                batch,
                labelC.getText(),
                posC.getPosition().x,
                posC.getPosition().y
        );
    }

    protected final boolean isWithinBounds(
            final float screenX,
            final float screenY
    ) {
        return !(screenX < -Constants.PPM * 2
                || screenX > Gdx.graphics.getWidth() + Constants.PPM
                || screenY < -Constants.PPM * 2
                || screenY > Gdx.graphics.getHeight() + Constants.PPM);
    }

    private float roundToPixels(final float val, final float zoom) {
        return ((int) (val * zoom)) / zoom;
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void begin() {
    }

    @Override
    protected void end() {
    }

    @Override
    protected final void dispose() {
        batch.dispose();
        disposePhysics();
        disposeGui();
        disposeMap();
    }

    protected abstract void disposePhysics();

    protected abstract void disposeGui();

    protected abstract void disposeMap();

    @Override
    protected void process(final Entity e) {
    }

    public final SpriteBatch getBatch() {
        return batch;
    }
}
