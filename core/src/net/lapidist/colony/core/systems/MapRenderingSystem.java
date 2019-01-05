package net.lapidist.colony.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.common.postprocessing.effects.Fxaa;
import net.lapidist.colony.common.postprocessing.effects.MotionBlur;
import net.lapidist.colony.core.ComponentMappers;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.components.SpriteComponent;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.core.Camera;
import net.lapidist.colony.core.core.Core;
import net.lapidist.colony.core.core.Graphics;
import net.lapidist.colony.core.input.InputManager;
import net.lapidist.colony.core.input.MapInputController;

public class MapRenderingSystem extends IteratingSystem {

    private Array<Entity> renderQueue;
    private MapInputController inputController;

    public MapRenderingSystem() {
        super(Family.all(TileComponent.class).get());

        renderQueue = new Array<>();
        inputController = new MapInputController(this);
        InputManager.add(inputController);

        MotionBlur motionBlur = new MotionBlur();
        Fxaa fxaa = new Fxaa(Graphics.width(), Graphics.height());

        motionBlur.setBlurOpacity(0.2f);

        Core.postProcessor.addEffect(motionBlur);
        Core.postProcessor.addEffect(fxaa);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (!renderQueue.contains(entity, true)) {
            SpriteComponent spriteC = ComponentMappers.sprites.get(entity);

            if (spriteC != null) renderQueue.add(entity);
        }
    }

    void draw() {
        Core.postProcessor.capture();

        for (Entity entity : renderQueue) {
            SpriteComponent spriteC = ComponentMappers.sprites.get(entity);
            TileComponent tileC = ComponentMappers.tiles.get(entity);

            if (tileC != inputController.getSelectedTile() && tileC.active) tileC.active = false;
            if (tileC != inputController.getHoveredTile() && tileC.hovered) tileC.hovered = false;

            Vector2 screenCoords = Camera.screenCoords(
                    tileC.tile.getBoundingBox().x,
                    tileC.tile.getBoundingBox().y
            );

            if (screenCoords.x < -Constants.PPM * 2
                    || screenCoords.x > Graphics.width() + Constants.PPM
                    || screenCoords.y < -Constants.PPM * 2
                    || screenCoords.y > Graphics.height() + Constants.PPM
            ) continue;

            if (spriteC != null) {
                Graphics.begin();
                Graphics.draw(spriteC.sprite);
                Graphics.end();
            }
        }

        Core.postProcessor.render();
    }

    public Array<Entity> getRenderQueue() {
        return renderQueue;
    }
}
