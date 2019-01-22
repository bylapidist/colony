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
import net.lapidist.colony.core.systems.abstracts.AbstractRenderSystem;
import net.lapidist.colony.core.systems.map.MapAssetSystem;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Origin;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.basic.Scale;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

@Wire
public class MapRenderSystem extends AbstractRenderSystem {

    private CameraSystem cameraSystem;
    private MapAssetSystem assetSystem;
    private Mappers mappers;

    public MapRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(Render.class).exclude(Invisible.class), principal);
    }

    @Override
    protected void initialize() {
        super.initialize();

        Events.on(MapInitEvent.class, mapInitEvent -> onInit());
        Events.on(ScreenResizeEvent.class, event -> onResize(event.getWidth(), event.getHeight()));
    }

    @Override
    protected void begin() {
        super.begin();
        batch.setProjectionMatrix(cameraSystem.camera.combined);
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected void process(final int e) {
        final Pos posC = mappers.mPos.get(e);
        final TextureComponent textureC = mappers.mTexture.get(e);
        final Angle angleC = mappers.mAngle.getSafe(e, Angle.NONE);
        final float scale = mappers.mScale.getSafe(e, Scale.DEFAULT).scale;
        final Origin originC = mappers.mOrigin.getSafe(e, defaultOrigin);

        batch.setColor(mappers.mTint.getSafe(e, Tint.WHITE).color);

        if (textureC != null && posC != null) drawTexture(textureC, angleC, originC, posC, scale, cameraSystem.zoom);
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    protected void onResize(int width, int height) {
        cameraSystem.camera.setToOrtho(true, width, height);
        cameraSystem.camera.update();
    }

    protected void onInit() {
        Entity e = world.createEntity(new ArchetypeBuilder()
                .add(Pos.class)
                .add(TextureComponent.class)
                .add(Render.class)
                .build(world));

        mappers.mPos.create(e);
        mappers.mTexture.create(e);

        mappers.mPos.get(e).set(new Vector3(0, 0, 1));
        mappers.mTexture.get(e).setTexture(assetSystem.getTexture("dirt"));
    }
}
