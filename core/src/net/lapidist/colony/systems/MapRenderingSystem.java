package net.lapidist.colony.systems;

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
import com.bitfire.postprocessing.effects.Fxaa;
import com.bitfire.postprocessing.effects.MotionBlur;
import net.lapidist.colony.components.ModelComponent;
import net.lapidist.colony.core.Camera;
import net.lapidist.colony.components.DecalComponent;
import net.lapidist.colony.components.TileComponent;
import net.lapidist.colony.core.Core;
import net.lapidist.colony.core.Graphics;
import net.lapidist.colony.input.MapInputController;

import static net.lapidist.colony.ComponentMappers.*;
import static net.lapidist.colony.Constants.*;

public class MapRenderingSystem extends IteratingSystem {

    private Array<Entity> renderQueue;
    private MapInputController inputController;
    private Decal selectedDecal;
    private Environment environment;

    public MapRenderingSystem() {
        super(Family.all(TileComponent.class).get());

        renderQueue = new Array<>();
        inputController = new MapInputController(this);
        environment = new Environment();

        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, PPM, PPM, -PPM));
        environment.add(new DirectionalLight().set(0.2f, 0.2f, 0.2f, -PPM * 4, -PPM, PPM));

        TextureRegion selectedTexture = resourceLoader.getRegion("selected");
        selectedDecal = Decal.newDecal(selectedTexture, true);

        Gdx.input.setInputProcessor(inputController);

        MotionBlur motionBlur = new MotionBlur();
        Fxaa fxaa = new Fxaa(Graphics.width(), Graphics.height());

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
            DecalComponent decalC = decals.get(entity);
            ModelComponent modelC = models.get(entity);

            if (decalC != null || modelC != null) renderQueue.add(entity);
        }
    }

    public void draw() {
        Core.postProcessor.capture();

        for (Entity entity : renderQueue) {
            DecalComponent decalC = decals.get(entity);
            ModelComponent modelC = models.get(entity);
            TileComponent tileC = tiles.get(entity);

            if (tileC != inputController.getSelectedTile() && tileC.active) tileC.active = false;
            if (tileC != inputController.getHoveredTile() && tileC.hovered) tileC.hovered = false;

            Vector2 screenCoords = Camera.screenCoords(
                tileC.bounds.getBoundingRectangle().x,
                tileC.bounds.getBoundingRectangle().y
            );

            if (
                screenCoords.x < -PPM * 2
                || screenCoords.x > Graphics.width() + PPM
                || screenCoords.y < -PPM * 2
                || screenCoords.y > Graphics.height() + PPM
            ) continue;

            if (decalC != null) {
                Graphics.add(decalC.decal);
            }

            if (tileC.hovered || tileC.active) {
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
