package net.lapidist.colony.client.renderers;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.components.entities.CelestialBodyComponent;

/** Draws celestial bodies like the sun and moon. */
public final class CelestialRenderer implements EntityRenderer<Void>, Disposable {
    private final SpriteBatch spriteBatch;
    private final ResourceLoader resourceLoader;
    private final World world;
    private final ComponentMapper<CelestialBodyComponent> bodyMapper;

    public CelestialRenderer(final SpriteBatch batch,
                             final ResourceLoader loader,
                             final World worldContext) {
        this.spriteBatch = batch;
        this.resourceLoader = loader;
        this.world = worldContext;
        this.bodyMapper = worldContext.getMapper(CelestialBodyComponent.class);
    }

    @Override
    public void render(final MapRenderData map) {
        var entities = world.getAspectSubscriptionManager()
                .get(Aspect.all(CelestialBodyComponent.class))
                .getEntities();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = world.getEntity(entities.get(i));
            CelestialBodyComponent body = bodyMapper.get(e);
            TextureRegion region = resourceLoader.findRegion(body.getTexture());
            if (region != null) {
                spriteBatch.draw(region, body.getX(), body.getY());
            }
        }
    }

    @Override
    public void dispose() {
        // no-op
    }
}
