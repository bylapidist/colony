package net.lapidist.colony.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.bitfire.postprocessing.effects.Fxaa;
import com.bitfire.postprocessing.effects.MotionBlur;
import net.lapidist.colony.core.ComponentMappers;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.components.DecalComponent;
import net.lapidist.colony.core.components.ModelComponent;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.core.Camera;
import net.lapidist.colony.core.core.Core;
import net.lapidist.colony.core.core.Graphics;
import net.lapidist.colony.core.input.InputManager;
import net.lapidist.colony.core.input.MapInputController;

public class MapRenderingSystem extends IteratingSystem {

    private Array<Entity> renderQueue;
    private MapInputController inputController;
    private Decal selectedDecal;
    private Decal spaceDecal;
    private Environment environment;

    public MapRenderingSystem() {
        super(Family.all(TileComponent.class).get());

        renderQueue = new Array<>();
        inputController = new MapInputController(this);
        environment = new Environment();
        InputManager.add(inputController);

        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, Constants.PPM, Constants.PPM, -Constants.PPM));

        TextureRegion selectedTexture = Constants.resourceLoader.getRegion("selected");
        selectedDecal = Decal.newDecal(selectedTexture, true);

        TextureRegion spaceTexture = Constants.resourceLoader.getRegion("space");
        spaceDecal = Decal.newDecal(spaceTexture, true);

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
            DecalComponent decalC = ComponentMappers.decals.get(entity);
            ModelComponent modelC = ComponentMappers.models.get(entity);

            if (decalC != null || modelC != null) renderQueue.add(entity);
        }
    }

    public void draw() {
        Core.postProcessor.capture();

        for (Entity entity : renderQueue) {
            DecalComponent decalC = ComponentMappers.decals.get(entity);
            ModelComponent modelC = ComponentMappers.models.get(entity);
            TileComponent tileC = ComponentMappers.tiles.get(entity);

            if (tileC != inputController.getSelectedTile() && tileC.active) tileC.active = false;
            if (tileC != inputController.getHoveredTile() && tileC.hovered) tileC.hovered = false;

            Vector2 screenCoords = Camera.screenCoords(
                tileC.bounds.getBoundingRectangle().x,
                tileC.bounds.getBoundingRectangle().y
            );

            if (
                screenCoords.x < -Constants.PPM * 2
                || screenCoords.x > Graphics.width() + Constants.PPM
                || screenCoords.y < -Constants.PPM * 2
                || screenCoords.y > Graphics.height() + Constants.PPM
            ) continue;

            if (tileC.active) {
                selectedDecal.setPosition(
                    tileC.bounds.getBoundingRectangle().x,
                    tileC.bounds.getBoundingRectangle().y,
                    0f
                );
                selectedDecal.setDimensions(
                    tileC.bounds.getBoundingRectangle().getWidth(),
                    tileC.bounds.getBoundingRectangle().getHeight()
                );
                Graphics.add(selectedDecal);
            } else if (tileC.hovered) {
                spaceDecal.setPosition(
                        tileC.bounds.getBoundingRectangle().x,
                        tileC.bounds.getBoundingRectangle().y,
                        0f
                );
                spaceDecal.setDimensions(
                        tileC.bounds.getBoundingRectangle().getWidth(),
                        tileC.bounds.getBoundingRectangle().getHeight()
                );
                Graphics.add(spaceDecal);
            }

            if (decalC != null) {
//                Graphics.add(decalC.decal);
            }

            Graphics.flush();

            if (modelC != null) {
                Core.modelBatch.begin(Core.camera);
                    Core.modelBatch.render(modelC.instance, environment);
                Core.modelBatch.end();
            }
        }

        Core.postProcessor.render();
    }

    public Array<Entity> getRenderQueue() {
        return renderQueue;
    }
}
