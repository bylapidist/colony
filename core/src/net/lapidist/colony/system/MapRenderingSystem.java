package net.lapidist.colony.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitfire.postprocessing.effects.Fxaa;
import com.bitfire.postprocessing.effects.MotionBlur;
import net.lapidist.colony.core.Camera;
import net.lapidist.colony.component.DecalComponent;
import net.lapidist.colony.component.TileComponent;
import net.lapidist.colony.core.Core;
import net.lapidist.colony.core.Graphics;
import net.lapidist.colony.input.MapInputController;

import static net.lapidist.colony.ComponentMappers.*;
import static net.lapidist.colony.Constants.PPM;
import static net.lapidist.colony.Constants.resourceLoader;

public class MapRenderingSystem extends IteratingSystem {

    private Array<Entity> renderQueue;
    private MapInputController inputController;
    private Decal selectedDecal;

    public MapRenderingSystem() {
        super(Family.all(TileComponent.class).get());

        renderQueue = new Array<>();
        inputController = new MapInputController(this);

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
            DecalComponent decalC = decal.get(entity);

            if (decalC != null) renderQueue.add(entity);
        }
    }

    public void draw() {
        Core.postProcessor.capture();

        for (Entity entity : renderQueue) {
            DecalComponent decalC = decal.get(entity);
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
        }

        Core.postProcessor.render();
    }

    public Array<Entity> getRenderQueue() {
        return renderQueue;
    }
}
