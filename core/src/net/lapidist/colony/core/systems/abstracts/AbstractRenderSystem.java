package net.lapidist.colony.core.systems.abstracts;

import com.artemis.Aspect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.components.assets.TextureComponent;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Origin;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

public abstract class AbstractRenderSystem extends DeferredEntityProcessingSystem {

    protected TextureRegion tmpTextureRegion;
    protected SpriteBatch batch;

    protected final Origin defaultOrigin = new Origin(0.5f, 0.5f);

    public AbstractRenderSystem(Aspect.Builder aspect, EntityProcessPrincipal principal) {
        super(aspect, principal);
    }

    protected abstract void onResize(int width, int height);

    protected abstract void onInit();

    protected void drawTexture(TextureComponent textureC, Angle angleC, Origin originC, Pos posC, float scale, float zoom) {
        float ox = textureC.getTexture().getWidth() * scale * originC.xy.x;
        float oy = textureC.getTexture().getHeight() * scale * originC.xy.y;
        tmpTextureRegion.setTexture(textureC.getTexture());

        if (angleC.rotation != 0) {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.xy.x, zoom),
                    roundToPixels(posC.xy.y, zoom),
                    ox,
                    oy,
                    textureC.getTexture().getWidth() * scale,
                    textureC.getTexture().getHeight() * scale, 1, 1,
                    angleC.rotation);
        } else {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.xy.x, zoom),
                    roundToPixels(posC.xy.y, zoom),
                    textureC.getTexture().getWidth() * scale,
                    textureC.getTexture().getHeight() * scale);
        }
    }

    protected void drawLabel(Label labelC, Angle angleC, Origin originC, Pos posC, float scale) {

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

    }

    @Override
    protected void process(int e) {

    }
}
