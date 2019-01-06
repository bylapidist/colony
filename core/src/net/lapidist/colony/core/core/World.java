package net.lapidist.colony.core.core;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.common.map.MapBuilder;
import net.lapidist.colony.common.map.MapLayout;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.common.map.tile.ITileGrid;
import net.lapidist.colony.common.map.tile.TileCoordinate;
import net.lapidist.colony.common.modules.Module;
import net.lapidist.colony.core.entities.EntityBuilder;
import net.lapidist.colony.core.entities.EntityType;
import net.lapidist.colony.core.entities.TerrainType;
import net.lapidist.colony.core.entities.UnitType;
import net.lapidist.colony.core.events.EventType.WorldInitEvent;
import net.lapidist.colony.core.systems.DebugRenderingSystem;
import net.lapidist.colony.core.systems.MapRenderingSystem;
import net.lapidist.colony.core.systems.PlayerSystem;
import net.lapidist.colony.core.systems.RenderingSystem;

import java.util.Optional;

import static net.lapidist.colony.core.Constants.*;

public class World extends Module {

    @Override
    public void init() {
        super.init();

        engine = new PooledEngine();

        engine.addSystem(new RenderingSystem());
        engine.addSystem(new DebugRenderingSystem());
        engine.addSystem(new MapRenderingSystem());
        engine.addSystem(new PlayerSystem());

        try {
            generateLevel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Events.fire(new WorldInitEvent());
    }

    @Override
    public void update() {
        super.update();

        engine.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void generateLevel() throws Exception {
        MapBuilder builder = new MapBuilder()
                .setGridHeight(12)
                .setGridWidth(12)
                .setTileWidth(PPM)
                .setTileHeight(PPM)
                .setMapLayout(MapLayout.RECTANGULAR);

        ITileGrid grid = builder.build();
        Iterable<ITile> tiles = grid.getTiles();

        tiles.forEach(tile -> {
            EntityBuilder entityBuilder = new EntityBuilder()
                    .setEngine(engine)
                    .setEntityType(EntityType.TERRAIN)
                    .setTerrainType(TerrainType.GRASS)
                    .setTile(tile);

            try {
                engine.addEntity(entityBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Optional<ITile> playerTile = grid.getByTileCoordinate(new TileCoordinate(0, 0, 0));

        EntityBuilder playerEntityBuilder = new EntityBuilder()
                .setEngine(engine)
                .setEntityType(EntityType.UNIT)
                .setUnitType(UnitType.PLAYER)
                .setTile(playerTile.get());

        engine.addEntity(playerEntityBuilder.build());
    }
}
