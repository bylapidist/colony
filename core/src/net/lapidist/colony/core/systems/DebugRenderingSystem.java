package net.lapidist.colony.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.core.ComponentMappers;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.components.ResourceComponent;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.core.Camera;
import net.lapidist.colony.core.core.Core;
import net.lapidist.colony.core.core.Graphics;
import net.lapidist.colony.common.resources.IResource;

public class DebugRenderingSystem extends IteratingSystem {

    private Array<Entity> renderQueue;
    private OrthographicCamera camera;
    private BitmapFont font;

    public DebugRenderingSystem() {
        super(Family.all(TileComponent.class).get());

        renderQueue = new Array<>();
        font = Constants.resourceLoader.getFont("default");
        camera = new OrthographicCamera(Graphics.width(), Graphics.height());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        camera.update();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (!renderQueue.contains(entity, true)) {
            renderQueue.add(entity);
        }
    }

    public void draw() {
        camera.setToOrtho(false, Graphics.width(), Graphics.height());
        Core.spriteBatch.setProjectionMatrix(camera.combined);
        Core.spriteBatch.begin();

            for (Entity entity : renderQueue) {
                ResourceComponent resourceC = ComponentMappers.resources.get(entity);

                if (resourceC != null) {
                    TileComponent tileC = ComponentMappers.tiles.get(entity);

                    int i = 0;
                    for (IResource resource : resourceC.getResources()) {
                        Vector2 screenCoords = Camera.screenCoords(tileC.bounds.getBoundingRectangle().x, tileC.bounds.getBoundingRectangle().y);

//                        font.draw(
//                            Core.spriteBatch,
//                            resource.getName() + ": " + resource.getComputedValue(),
//                            screenCoords.x,
//                            screenCoords.y - i
//                        );

                        i += 16;
                    }
                }
            }

            font.draw(
                Core.spriteBatch,
                Gdx.graphics.getFramesPerSecond() + " FPS",
                32,
                Graphics.height() - 32
            );

        Core.spriteBatch.end();
    }
}
