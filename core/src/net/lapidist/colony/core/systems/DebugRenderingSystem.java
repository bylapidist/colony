package net.lapidist.colony.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.core.Core;
import net.lapidist.colony.core.core.Graphics;

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

        font.draw(
                Core.spriteBatch,
                Gdx.graphics.getFramesPerSecond() + " FPS",
                32,
                Graphics.height() - 32
        );

        Core.spriteBatch.end();
    }
}
