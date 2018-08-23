package net.lapidist.colony.core;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Polygon;
import net.lapidist.colony.component.*;
import net.lapidist.colony.event.EventType.WorldInitEvent;
import net.lapidist.colony.event.Events;
import net.lapidist.colony.game.resources.*;
import net.lapidist.colony.grid.*;
import net.lapidist.colony.grid.hex.CubeCoordinate;
import net.lapidist.colony.grid.hex.HexagonOrientation;
import net.lapidist.colony.grid.hex.IHexagon;
import net.lapidist.colony.grid.hex.IHexagonalGrid;
import net.lapidist.colony.module.Module;
import net.lapidist.colony.system.DebugRenderingSystem;
import net.lapidist.colony.system.MapRenderingSystem;
import net.lapidist.colony.system.RenderingSystem;
import net.lapidist.colony.utils.Optional;

import static net.lapidist.colony.Constants.*;

public class World extends Module {

    @Override
    public void init() {
        super.init();

        engine = new PooledEngine();

        engine.addSystem(new RenderingSystem());
        engine.addSystem(new DebugRenderingSystem());
        engine.addSystem(new MapRenderingSystem());

        generateLevel();

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

    private void generateLevel() {
        GridBuilder builder = new GridBuilder()
            .setGridHeight(12)
            .setGridWidth(12)
            .setGridLayout(GridLayout.RECTANGULAR)
            .setOrientation(HexagonOrientation.POINTY_TOP)
            .setRadius(PPM);

        IHexagonalGrid grid = builder.build();

        Iterable<IHexagon> hexagons = grid.getHexagons();

        hexagons.forEach(hexagon -> {
            Entity tile = createHex(hexagon);

            engine.addEntity(tile);
        });

        Optional<IHexagon> hex = grid.getByCubeCoordinate(new CubeCoordinate(3, 6));
        Optional<IHexagon> hex2 = grid.getByCubeCoordinate(new CubeCoordinate(6, 7));

        Entity star = createStar(hex.get());
        engine.addEntity(star);

        Entity planet = createPlanet(hex2.get());
        engine.addEntity(planet);
    }

    private Entity createPlanet(IHexagon hex) {
        Entity entity = engine.createEntity();
        TextureRegion texture = resourceLoader.getRegion("smallplanet");

        DecalComponent decalC = engine.createComponent(DecalComponent.class);
        TileComponent tileC = engine.createComponent(TileComponent.class);
        ResourceComponent resourceC = engine.createComponent(ResourceComponent.class);

        tileC.hex = hex;
        tileC.bounds = new Polygon(hex.getVertices());
        decalC.decal = Decal.newDecal(texture, true);
        decalC.decal.setPosition(
            tileC.bounds.getBoundingRectangle().x,
            tileC.bounds.getBoundingRectangle().y,
            0f
        );
        decalC.decal.setDimensions(
            tileC.bounds.getBoundingRectangle().getWidth(),
            tileC.bounds.getBoundingRectangle().getHeight()
        );

        resourceC.addResource(new EnergyResource(5f));
        resourceC.addResource(new FoodResource(0f));
        resourceC.addResource(new InfluenceResource(0f));
        resourceC.addResource(new MoneyResource(0f));
        resourceC.addResource(new ProductionResource(0f));
        resourceC.addResource(new ScienceResource(0f));

        entity.add(decalC);
        entity.add(tileC);
        entity.add(resourceC);

        Core.camera.tweenToTile(tileC);

        return entity;
    }

    private Entity createStar(IHexagon hex) {
        Entity entity = engine.createEntity();
        TextureRegion texture = resourceLoader.getRegion("star");

        DecalComponent decalC = engine.createComponent(DecalComponent.class);
        TileComponent tileC = engine.createComponent(TileComponent.class);
        ResourceComponent resourceC = engine.createComponent(ResourceComponent.class);

        tileC.hex = hex;
        tileC.bounds = new Polygon(hex.getVertices());
        decalC.decal = Decal.newDecal(texture, true);
        decalC.decal.setPosition(
            tileC.bounds.getBoundingRectangle().x,
            tileC.bounds.getBoundingRectangle().y,
            0f
        );
        decalC.decal.setDimensions(
            tileC.bounds.getBoundingRectangle().getWidth(),
            tileC.bounds.getBoundingRectangle().getHeight()
        );

        resourceC.addResource(new EnergyResource(5f));
        resourceC.addResource(new FoodResource(0f));
        resourceC.addResource(new InfluenceResource(0f));
        resourceC.addResource(new MoneyResource(0f));
        resourceC.addResource(new ProductionResource(0f));
        resourceC.addResource(new ScienceResource(0f));

        entity.add(decalC);
        entity.add(tileC);
        entity.add(resourceC);

        Core.camera.tweenToTile(tileC);

        return entity;
    }

    private Entity createHex(IHexagon hex) {
        Entity entity = engine.createEntity();
        TextureRegion texture = resourceLoader.getRegion("space");

        DecalComponent decalC = engine.createComponent(DecalComponent.class);
        TileComponent tileC = engine.createComponent(TileComponent.class);

        tileC.hex = hex;
        tileC.bounds = new Polygon(hex.getVertices());
        decalC.decal = Decal.newDecal(texture, true);
        decalC.decal.setPosition(
            tileC.bounds.getBoundingRectangle().x,
            tileC.bounds.getBoundingRectangle().y,
            0f
        );
        decalC.decal.setDimensions(
            tileC.bounds.getBoundingRectangle().getWidth(),
            tileC.bounds.getBoundingRectangle().getHeight()
        );

        entity.add(decalC);
        entity.add(tileC);

        return entity;
    }
}
