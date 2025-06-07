package net.lapidist.colony.client.ui;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.lapidist.colony.client.core.Constants;
import net.lapidist.colony.client.core.io.FileLocation;
import net.lapidist.colony.client.core.io.ResourceLoader;
import net.lapidist.colony.components.assets.TextureRegionReferenceComponent;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;

import java.io.IOException;

/**
 * Draws a minimap representing the current map.
 */
public final class MinimapActor extends Actor implements Disposable {

    private static final int DEFAULT_SIZE = 128;

    private final World world;
    private final ResourceLoader resourceLoader = new ResourceLoader();
    private ComponentMapper<MapComponent> mapMapper;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<TextureRegionReferenceComponent> textureMapper;
    private Entity map;

    private void initMappers() {
        if (mapMapper == null) {
            mapMapper = world.getMapper(MapComponent.class);
            tileMapper = world.getMapper(TileComponent.class);
            textureMapper = world.getMapper(TextureRegionReferenceComponent.class);
            IntBag maps = world.getAspectSubscriptionManager()
                    .get(Aspect.all(MapComponent.class))
                    .getEntities();
            if (maps.size() > 0) {
                map = world.getEntity(maps.get(0));
            }
        }
    }

    public MinimapActor(final World worldToSet) {
        this.world = worldToSet;
        setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        try {
            resourceLoader.load(FileLocation.INTERNAL, "resources.conf");
        } catch (IOException e) {
            // ignore loading errors in headless tests
        }
    }

    @Override
    protected void setStage(final com.badlogic.gdx.scenes.scene2d.Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            initMappers();
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        if (!(batch instanceof SpriteBatch)) {
            return;
        }

        initMappers();
        if (map == null) {
            return;
        }

        float scaleX = getWidth() / (Constants.MAP_WIDTH * Constants.TILE_SIZE);
        float scaleY = getHeight() / (Constants.MAP_HEIGHT * Constants.TILE_SIZE);

        Array<Entity> tiles = mapMapper.get(map).getTiles();
        for (int i = 0; i < tiles.size; i++) {
            Entity tile = tiles.get(i);
            TileComponent tileComponent = tileMapper.get(tile);
            TextureRegion region = resourceLoader.getTextureRegions()
                    .get(textureMapper.get(tile).getResourceRef());
            if (region != null) {
                batch.draw(
                        region,
                        getX() + tileComponent.getX() * Constants.TILE_SIZE * scaleX,
                        getY() + tileComponent.getY() * Constants.TILE_SIZE * scaleY,
                        Constants.TILE_SIZE * scaleX,
                        Constants.TILE_SIZE * scaleY
                );
            }
        }
    }

    @Override
    public void dispose() {
        resourceLoader.dispose();
    }
}
