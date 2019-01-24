package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.base.RotationComponent;
import net.lapidist.colony.components.base.OriginComponent;
import net.lapidist.colony.components.base.PositionComponent;
import net.lapidist.colony.components.base.ScaleComponent;
import net.lapidist.colony.components.gui.LabelComponent;
import net.lapidist.colony.core.Constants;

public abstract class AbstractRenderSystem extends EntityProcessingSystem {

    protected SpriteBatch batch;
    private TextureRegion tmpTextureRegion;

    public AbstractRenderSystem(Aspect.Builder aspect) {
        super(aspect);

        tmpTextureRegion = new TextureRegion();
        batch = new SpriteBatch(2000);
    }

    protected abstract void onResize(int width, int height);

    protected abstract void onInit();

    protected void drawTexture(
            TextureComponent textureC,
            RotationComponent angleC,
            OriginComponent originC,
            PositionComponent posC,
            ScaleComponent scaleC,
            float zoom
    ) {
        float ox = textureC.getTexture().getWidth() * scaleC.getScale() * originC.getOrigin().x;
        float oy = textureC.getTexture().getHeight() * scaleC.getScale() * originC.getOrigin().y;

        tmpTextureRegion.setTexture(textureC.getTexture());
        tmpTextureRegion.setRegionWidth(textureC.getTexture().getWidth());
        tmpTextureRegion.setRegionHeight(textureC.getTexture().getHeight());

        if (angleC.getRotation() != 0) {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.getPosition().x, zoom),
                    roundToPixels(posC.getPosition().y, zoom),
                    ox,
                    oy,
                    tmpTextureRegion.getRegionWidth() * scaleC.getScale(),
                    tmpTextureRegion.getRegionHeight() * scaleC.getScale(),
                    1,
                    1,
                    angleC.getRotation()
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

    protected void drawLabel(
            LabelComponent labelC,
            FontComponent fontC,
            PositionComponent posC
    ) {
        fontC.getFont().draw(
                batch,
                labelC.getText(),
                posC.getPosition().x,
                posC.getPosition().y
        );
    }

    protected boolean isWithinBounds(final float x, final float y) {
        return !(x < -Constants.PPM * 2
                || x > Gdx.graphics.getWidth() + Constants.PPM
                || y < -Constants.PPM * 2
                || y > Gdx.graphics.getHeight() + Constants.PPM);
    }

    private float roundToPixels(final float val, final float zoom) {
        return ((int)(val * zoom)) / zoom;
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
    protected void dispose() {
        batch.dispose();
    }

    @Override
    protected void process(Entity e) {

    }
}
