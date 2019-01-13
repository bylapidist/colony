package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.common.map.MapBuilder;
import net.lapidist.colony.common.map.MapLayout;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.common.map.tile.ITileGrid;
import net.lapidist.colony.common.map.tile.TileCoordinate;
import net.lapidist.colony.common.postprocessing.effects.Fxaa;
import net.lapidist.colony.common.postprocessing.effects.MotionBlur;
import net.lapidist.colony.components.*;
import net.lapidist.colony.components.archetypes.UnitType;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.Constants;
import net.lapidist.colony.components.archetypes.EntityFactory;
import net.lapidist.colony.components.archetypes.TerrainType;
import net.lapidist.colony.core.systems.camera.CameraSystem;

import java.util.Optional;

import static com.artemis.E.*;

@Wire
public class MapRenderingSystem extends EntityProcessingSystem implements Screen {

    private Array<Entity> renderQueue;
    private static Vector3 tmpVec3 = new Vector3();
    private static Vector2 mouse = new Vector2();
    private CameraSystem cameraSystem;

    public MapRenderingSystem() {
        super(Aspect.all(RenderableComponent.class));

        this.renderQueue = new Array<>();

        MotionBlur motionBlur = new MotionBlur();
        Fxaa fxaa = new Fxaa(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        motionBlur.setBlurOpacity(0.2f);

        Colony.getPostProcessor().addEffect(motionBlur);
        Colony.getPostProcessor().addEffect(fxaa);
    }

    @Override
    protected void process(Entity entity) {
        if (!renderQueue.contains(entity, true)) {
            renderQueue.add(entity);
        }
    }

    @Override
    public void dispose() {
        renderQueue.forEach(Entity::deleteFromWorld);
        renderQueue.clear();
    }

    @Override
    public void show() {
        generateLevel();
    }

    @Override
    public void hide() {
    }

    @Override
    public void render(float delta) {
        cameraSystem.camera.update();

        Colony.getPostProcessor().capture();
        Colony.getSpriteBatch().setProjectionMatrix(cameraSystem.camera.combined);
        Colony.getSpriteBatch().begin();

        drawTerrain();
        drawBuildings();
        drawUnits();

        Colony.getSpriteBatch().end();
        Colony.getPostProcessor().render();
    }

    @Override
    public void resize(int width, int height) {
        cameraSystem.camera.setToOrtho(true, width, height);
        cameraSystem.camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    private void drawTerrain() {
        for (Entity entity : renderQueue) {
            if (E(entity).hasTerrainComponent()) {
                if (outOfBounds(
                        E(entity).getSpriteComponent().getSprite().getX(),
                        E(entity).getSpriteComponent().getSprite().getY()
                )) continue;

                E(entity).getSpriteComponent().getSprite()
                        .draw(Colony.getSpriteBatch());
            }
        }
    }

    private void drawBuildings() {
        for (Entity entity : renderQueue) {
            if (E(entity).hasBuildingComponent()) {
                if (outOfBounds(
                        E(entity).getSpriteComponent().getSprite().getX(),
                        E(entity).getSpriteComponent().getSprite().getY()
                )) continue;

                E(entity).getSpriteComponent().getSprite()
                        .draw(Colony.getSpriteBatch());
            }
        }
    }

    private void drawUnits() {
        for (Entity entity : renderQueue) {
            if (E(entity).hasUnitComponent()) {
                if (outOfBounds(
                        E(entity).getSpriteComponent().getSprite().getX(),
                        E(entity).getSpriteComponent().getSprite().getY()
                )) continue;

                E(entity).getSpriteComponent().getSprite()
                        .draw(Colony.getSpriteBatch());
            }
        }
    }

    private void generateLevel() {
        MapBuilder builder = new MapBuilder()
                .setGridHeight(120)
                .setGridWidth(120)
                .setTileWidth(Constants.PPM)
                .setTileHeight(Constants.PPM)
                .setMapLayout(MapLayout.RECTANGULAR);

        ITileGrid grid = builder.build();
        Iterable<ITile> tiles = grid.getTiles();

        tiles.forEach(tile -> {
            Entity entity = new EntityFactory().createTerrain(world);

            Sprite sprite = new Sprite(Colony.getResourceLoader().getRegion("empty"));
            sprite.setBounds(
                    tile.getBoundingBox().getX(),
                    tile.getBoundingBox().getY(),
                    tile.getBoundingBox().getWidth(),
                    tile.getBoundingBox().getHeight()
            );

            E(entity).nameComponentName(tile.getId())
                    .tileComponentTile(tile)
                    .terrainComponentTerrainType(TerrainType.EMPTY)
                    .spriteComponentSprite(sprite);
        });

        Optional<ITile> playerTile = grid.getByTileCoordinate(new TileCoordinate(0, 0 , 0));
        Entity playerEntity = new EntityFactory().createPlayer(world);
        Sprite playerSprite = new Sprite(Colony.getResourceLoader().getRegion("grass"));
        playerSprite.setBounds(
                playerTile.get().getBoundingBox().getX(),
                playerTile.get().getBoundingBox().getY(),
                playerTile.get().getBoundingBox().getWidth(),
                playerTile.get().getBoundingBox().getHeight()
        );

        E(playerEntity).nameComponentName("Player")
                .unitComponentUnitType(UnitType.PLAYER)
                .tileComponentTile(playerTile.get())
                .spriteComponentSprite(playerSprite);
    }

    private boolean outOfBounds(float x, float y) {
        Vector2 screenCoords = screenCoords(x, y);

        return screenCoords.x < -Constants.PPM * 2
                || screenCoords.x > Gdx.graphics.getWidth() + Constants.PPM
                || screenCoords.y < -Constants.PPM * 2
                || screenCoords.y > Gdx.graphics.getHeight() + Constants.PPM;
    }

    private Vector2 screenCoords(float worldX, float worldY) {
        cameraSystem.camera.project(tmpVec3.set(worldX, worldY, 0));

        return mouse.set(tmpVec3.x, tmpVec3.y);
    }
}
