package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapProperties;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.components.render.GuiComponent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.events.HoverTileOutsideReachEvent;
import net.lapidist.colony.core.events.HoverTileWithinReachEvent;
import net.lapidist.colony.core.events.ScreenResizeEvent;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.logic.GuiEntityFactorySystem;
import net.lapidist.colony.core.systems.logic.MapGenerationSystem;

import static com.artemis.E.*;

@Wire
public class GuiRenderingSystem extends EntityProcessingSystem {

    private BitmapFont font;
    private CameraSystem cameraSystem;
    private GuiEntityFactorySystem guiEntityFactorySystem;
    private MapGenerationSystem mapGenerationSystem;
    private Entity hoveredTileEntity;

    public GuiRenderingSystem() {
        super(Aspect.all(
                GuiComponent.class
        ));
    }

    @Override
    protected void initialize() {
        Events.on(ScreenResizeEvent.class, event -> resize(event.width, event.height));
        Events.on(HoverTileWithinReachEvent.class, event -> {
            if (hoveredTileEntity == null) {
                MapProperties properties = new MapProperties();
                properties.put("tileWidth", mapGenerationSystem.getTileWidth());
                properties.put("tileHeight", mapGenerationSystem.getTileHeight());
                hoveredTileEntity = guiEntityFactorySystem.createEntity(
                        "hoveredTile",
                        event.getGridX() * mapGenerationSystem.getTileWidth(),
                        event.getGridY() * mapGenerationSystem.getTileHeight(),
                        properties
                );
            } else {
                E(hoveredTileEntity).spriteComponentSprite()
                        .setPosition(
                                event.getGridX() * mapGenerationSystem.getTileWidth(),
                                event.getGridY() * mapGenerationSystem.getTileHeight()
                        );
            }

            E(hoveredTileEntity).guiComponentRenderable(true);
        });
        Events.on(HoverTileOutsideReachEvent.class, event -> {
            if (hoveredTileEntity != null) {
                E(hoveredTileEntity).guiComponentRenderable(false);
            }
        });

        font = Colony.getResourceLoader().getFont("default");
    }

    @Override
    protected void process(Entity e) {
        if (E(e).hasSpriteComponent() && E(e).guiComponentIsRenderable()) {
            Colony.getSpriteBatch().setProjectionMatrix(cameraSystem.camera.combined);

            E(e).spriteComponentSprite().draw(Colony.getSpriteBatch());

            Colony.getSpriteBatch().setProjectionMatrix(cameraSystem.guiCamera.combined);
        }
    }

    @Override
    protected void begin() {
        Colony.getSpriteBatch().setProjectionMatrix(cameraSystem.guiCamera.combined);
        Colony.getSpriteBatch().begin();

        font.draw(
                Colony.getSpriteBatch(),
                Gdx.graphics.getFramesPerSecond() + " FPS",
                32,
                Gdx.graphics.getHeight() - 32
        );
    }

    @Override
    protected void end() {
        Colony.getSpriteBatch().end();
    }

    private void resize(int width, int height) {
        cameraSystem.guiCamera.setToOrtho(false, width, height);
        cameraSystem.guiCamera.update();
    }
}
