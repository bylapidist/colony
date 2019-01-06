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
import net.lapidist.colony.core.components.traits.SpriteTrait;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.core.Camera;
import net.lapidist.colony.core.core.Core;
import net.lapidist.colony.core.core.Graphics;
import net.lapidist.colony.core.input.InputManager;
import net.lapidist.colony.core.input.MapInputController;

public class MapRenderingSystem extends IteratingSystem {

    private Array<Entity> renderQueue;

    public MapRenderingSystem() {
        super(Family.all(TileComponent.class).get());

        renderQueue = new Array<>();
        MapInputController inputController = new MapInputController(this);
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
            SpriteTrait spriteC = ComponentMappers.sprites.get(entity);

            if (spriteC != null) renderQueue.add(entity);
        }
    }

    void draw() {
        Core.postProcessor.capture();

        Graphics.begin();
        drawTerrain();
        drawUnits();
        Graphics.end();

        Core.postProcessor.render();
    }

    private void drawTerrain() {
        for (Entity entity : renderQueue) {
            TileComponent tileC = ComponentMappers.tiles.get(entity);

            if (tileC.terrainType != null) {
                SpriteTrait spriteC = ComponentMappers.sprites.get(entity);

                if (outOfBounds(
                        tileC.tile.getBoundingBox().x,
                        tileC.tile.getBoundingBox().y
                )) continue;

                Graphics.draw(spriteC.sprite);
            }
        }
    }

    private void drawUnits() {
        for (Entity entity : renderQueue) {
            TileComponent tileC = ComponentMappers.tiles.get(entity);

            if (tileC.unitType != null) {
                SpriteTrait spriteC = ComponentMappers.sprites.get(entity);

                if (outOfBounds(
                        tileC.tile.getBoundingBox().x,
                        tileC.tile.getBoundingBox().y
                )) continue;

                Graphics.draw(spriteC.sprite);
            }
        }
    }

    private boolean outOfBounds(float x, float y) {
        Vector2 screenCoords = Camera.screenCoords(x, y);

        return screenCoords.x < -Constants.PPM * 2
                || screenCoords.x > Graphics.width() + Constants.PPM
                || screenCoords.y < -Constants.PPM * 2
                || screenCoords.y > Graphics.height() + Constants.PPM;
    }

    public Array<Entity> getRenderQueue() {
        return renderQueue;
    }
}
