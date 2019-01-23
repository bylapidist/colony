package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import net.lapidist.colony.components.assets.FontComponent;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.components.base.RotationComponent;
import net.lapidist.colony.components.base.OriginComponent;
import net.lapidist.colony.components.base.PositionComponent;
import net.lapidist.colony.components.base.ScaleComponent;
import net.lapidist.colony.components.gui.LabelComponent;
import net.lapidist.colony.core.systems.delegate.DeferredEntityProcessingSystem;
import net.lapidist.colony.core.systems.delegate.EntityProcessPrincipal;

public abstract class AbstractRenderSystem extends DeferredEntityProcessingSystem {

    protected final Vector2 defaultOrigin = new Vector2(0.5f, 0.5f);

    protected SpriteBatch batch;
    private TextureRegion tmpTextureRegion;

    public AbstractRenderSystem(Aspect.Builder aspect, EntityProcessPrincipal principal) {
        super(aspect, principal);

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

        if (angleC.getRotation() != 0) {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.getPosition().x, zoom),
                    roundToPixels(posC.getPosition().y, zoom),
                    ox,
                    oy,
                    textureC.getTexture().getWidth() * scaleC.getScale(),
                    textureC.getTexture().getHeight() * scaleC.getScale(), 1, 1,
                    angleC.getRotation());
        } else {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.getPosition().x, zoom),
                    roundToPixels(posC.getPosition().y, zoom),
                    textureC.getTexture().getWidth() * scaleC.getScale(),
                    textureC.getTexture().getHeight() * scaleC.getScale());
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

    private float roundToPixels(final float val, final float zoom) {
        return ((int)(val * zoom)) / zoom;
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void begin() {
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void dispose() {
        batch.dispose();
    }

    @Override
    protected void process(int e) {

    }
}
