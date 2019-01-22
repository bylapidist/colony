package net.lapidist.colony.core.systems.render;

import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.components.assets.TextureComponent;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.logic.MapInitEvent;
import net.lapidist.colony.core.events.render.ScreenResizeEvent;
import net.lapidist.colony.core.systems.Mappers;
import net.lapidist.colony.core.systems.map.MapAssetSystem;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Origin;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.basic.Scale;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

@Wire
public class MapRenderSystem extends DeferredEntityProcessingSystem {

    private TextureRegion tmpTextureRegion;
    private SpriteBatch batch;
    private final Origin defaultOrigin = new Origin(0.5f, 0.5f);

    private CameraSystem cameraSystem;
    private MapAssetSystem assetSystem;
    private Mappers mappers;

    public MapRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(Pos.class, TextureComponent.class, Render.class).exclude(Invisible.class), principal);
    }

    @Override
    protected void initialize() {
        super.initialize();

        tmpTextureRegion = new TextureRegion();
        batch = new SpriteBatch(2000);

        Events.on(MapInitEvent.class, mapInitEvent -> {
            Entity e = world.createEntity(new ArchetypeBuilder()
                    .add(Pos.class)
                    .add(TextureComponent.class)
                    .add(Render.class)
                    .build(world));

            mappers.mPos.create(e);
            mappers.mTexture.create(e);

            mappers.mPos.get(e).set(new Vector3(0, 0, 1));
            mappers.mTexture.get(e).setTexture(assetSystem.getTexture("player"));

            Entity e2 = world.createEntity(new ArchetypeBuilder()
                    .add(Pos.class)
                    .add(TextureComponent.class)
                    .add(Render.class)
                    .build(world));

            mappers.mPos.create(e2);
            mappers.mTexture.create(e2);
            mappers.mPos.get(e2).set(new Vector3(0, 0, 0));
            mappers.mTexture.get(e2).setTexture(assetSystem.getTexture("dirt"));

            Events.on(ScreenResizeEvent.class, event -> {
                cameraSystem.camera.setToOrtho(true, event.getWidth(), event.getHeight());
                cameraSystem.camera.update();
            });
        });
    }

    @Override
    protected void begin() {
        cameraSystem.camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraSystem.camera.update();
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void process(final int e) {
        final Pos posC = mappers.mPos.get(e);
        final TextureComponent textureC = mappers.mTexture.get(e);
        final Angle angleC = mappers.mAngle.getSafe(e, Angle.NONE);
        final float scale = mappers.mScale.getSafe(e, Scale.DEFAULT).scale;
        final Origin originC = mappers.mOrigin.getSafe(e, defaultOrigin);

        batch.setColor(mappers.mTint.getSafe(e, Tint.WHITE).color);

        if (textureC != null && posC != null) drawTexture(textureC, angleC, originC, posC, scale);
    }

    @Override
    protected void dispose() {
        batch.dispose();
    }

    private float roundToPixels(final float val) {
        return ((int)(val * cameraSystem.zoom)) / cameraSystem.zoom;
    }

    private void drawTexture(TextureComponent textureC, Angle angleC, Origin originC, Pos posC, float scale) {
        float ox = textureC.getTexture().getWidth() * scale * originC.xy.x;
        float oy = textureC.getTexture().getHeight() * scale * originC.xy.y;
        tmpTextureRegion.setTexture(textureC.getTexture());

        if (angleC.rotation != 0) {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.xy.x),
                    roundToPixels(posC.xy.y),
                    ox,
                    oy,
                    textureC.getTexture().getWidth() * scale,
                    textureC.getTexture().getHeight() * scale, 1, 1,
                    angleC.rotation);
        } else {
            batch.draw(tmpTextureRegion,
                    roundToPixels(posC.xy.x),
                    roundToPixels(posC.xy.y),
                    textureC.getTexture().getWidth() * scale,
                    textureC.getTexture().getHeight() * scale);
        }
    }
}
