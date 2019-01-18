package net.lapidist.colony.core.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.common.map.tile.TileCoordinate;
import net.lapidist.colony.common.postprocessing.effects.Fxaa;
import net.lapidist.colony.common.postprocessing.effects.MotionBlur;
import net.lapidist.colony.components.archetypes.TerrainType;
import net.lapidist.colony.components.archetypes.UnitType;
import net.lapidist.colony.components.render.RenderableComponent;
import net.lapidist.colony.components.render.UpdatableComponent;
import net.lapidist.colony.core.Colony;
import net.lapidist.colony.core.systems.EntityFactory;
import net.lapidist.colony.core.events.ScreenResizeEvent;
import net.lapidist.colony.core.systems.camera.CameraSystem;
import net.lapidist.colony.core.systems.logic.MapGenerationSystem;

import java.util.Optional;

import static com.artemis.E.*;

@Wire
public class MapRenderingSystem extends EntityProcessingSystem {

    private Array<Entity> renderQueue;
    private CameraSystem cameraSystem;
    private MapGenerationSystem mapGenerationSystem;

    public MapRenderingSystem() {
        super(Aspect.all(RenderableComponent.class, UpdatableComponent.class));

        this.renderQueue = new Array<>();

        MotionBlur motionBlur = new MotionBlur();
        Fxaa fxaa = new Fxaa(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        motionBlur.setBlurOpacity(0.2f);

        Colony.getPostProcessor().addEffect(motionBlur);
        Colony.getPostProcessor().addEffect(fxaa);
    }

    @Override
    protected void initialize() {
        Events.on(ScreenResizeEvent.class, event -> {
            resize(event.width, event.height);
        });

        Iterable<ITile> tiles = mapGenerationSystem.getGrid().getTiles();

        tiles.forEach(tile -> {
            Entity entity = new EntityFactory().createTerrain(world);

            Sprite sprite = new Sprite(Colony.getResourceLoader().getTexture("empty"));
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

        Optional<ITile> playerTile = mapGenerationSystem.getGrid()
                .getByTileCoordinate(new TileCoordinate(0, 0 , 0));
        Entity playerEntity = new EntityFactory().createPlayer(world);
        Sprite playerSprite = new Sprite(Colony.getResourceLoader().getTexture("grass"));
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

    @Override
    protected void process(Entity entity) {
        if (!renderQueue.contains(entity, true)) {
            if (cameraSystem.outOfBounds(
                    E(entity).getSpriteComponent().getSprite().getX(),
                    E(entity).getSpriteComponent().getSprite().getY()
            )) return;

            renderQueue.add(entity);
        }
    }

    @Override
    protected void begin() {
        render(world.getDelta());
    }

    @Override
    public void dispose() {
        renderQueue.forEach(Entity::deleteFromWorld);
        renderQueue.clear();
    }

    private void render(float delta) {
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

    private void resize(int width, int height) {
        cameraSystem.camera.setToOrtho(true, width, height);
        cameraSystem.camera.update();
    }

    private void drawTerrain() {
        for (Entity entity : renderQueue) {
            if (E(entity).hasTerrainComponent()) {
                E(entity).getSpriteComponent().getSprite()
                        .draw(Colony.getSpriteBatch());
            }
        }
    }

    private void drawBuildings() {
        for (Entity entity : renderQueue) {
            if (E(entity).hasBuildingComponent()) {
                E(entity).getSpriteComponent().getSprite()
                        .draw(Colony.getSpriteBatch());
            }
        }
    }

    private void drawUnits() {
        for (Entity entity : renderQueue) {
            if (E(entity).hasUnitComponent()) {
                E(entity).getSpriteComponent().getSprite()
                        .draw(Colony.getSpriteBatch());
            }
        }
    }
}
