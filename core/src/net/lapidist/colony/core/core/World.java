package net.lapidist.colony.core.core;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.core.components.DecalComponent;
import net.lapidist.colony.core.components.ModelComponent;
import net.lapidist.colony.core.components.ResourceComponent;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.common.resources.*;
import net.lapidist.colony.common.grid.GridBuilder;
import net.lapidist.colony.common.grid.GridLayout;
import net.lapidist.colony.core.events.EventType.WorldInitEvent;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.common.grid.hex.CubeCoordinate;
import net.lapidist.colony.common.grid.hex.HexagonOrientation;
import net.lapidist.colony.common.grid.hex.IHexagon;
import net.lapidist.colony.common.grid.hex.IHexagonalGrid;
import net.lapidist.colony.common.modules.Module;
import net.lapidist.colony.core.systems.DebugRenderingSystem;
import net.lapidist.colony.core.systems.MapRenderingSystem;
import net.lapidist.colony.core.systems.PlayerSystem;
import net.lapidist.colony.core.systems.RenderingSystem;
import net.lapidist.colony.common.utils.Optional;

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

        Entity player = createPlayer();
        engine.addEntity(player);
    }

    private Entity createPlayer() {
        Entity entity = engine.createEntity();

        ResourceComponent resourceC = engine.createComponent(ResourceComponent.class);

        resourceC.addResource(new EnergyResource(0f));
        resourceC.addResource(new FoodResource(0f));
        resourceC.addResource(new InfluenceResource(0f));
        resourceC.addResource(new MoneyResource(0f));
        resourceC.addResource(new ProductionResource(0f));
        resourceC.addResource(new ScienceResource(0f));

        entity.add(resourceC);

        return entity;
    }

    private Entity createPlanet(IHexagon hex) {
        Entity entity = engine.createEntity();

        ModelComponent modelC = engine.createComponent(ModelComponent.class);
        TileComponent tileC = engine.createComponent(TileComponent.class);
        ResourceComponent resourceC = engine.createComponent(ResourceComponent.class);

        tileC.hex = hex;
        tileC.bounds = new Polygon(hex.getVertices());

        Vector3 position = new Vector3(
            tileC.bounds.getBoundingRectangle().x,
            tileC.bounds.getBoundingRectangle().y,
            PPM / 2
        );

        ModelBuilder modelBuilder = new ModelBuilder();
        modelC.model = modelBuilder.createSphere(
            PPM / 2, PPM / 2, PPM / 2, 16, 16,
            new Material(ColorAttribute.createDiffuse(Color.BLUE)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        modelC.instance = new ModelInstance(modelC.model);
        modelC.instance.transform.setTranslation(position);

        resourceC.addResource(new EnergyResource(5f));
        resourceC.addResource(new FoodResource(0f));
        resourceC.addResource(new InfluenceResource(0f));
        resourceC.addResource(new MoneyResource(0f));
        resourceC.addResource(new ProductionResource(0f));
        resourceC.addResource(new ScienceResource(0f));

        entity.add(modelC);
        entity.add(tileC);
        entity.add(resourceC);

        return entity;
    }

    private Entity createStar(IHexagon hex) {
        Entity entity = engine.createEntity();

        ModelComponent modelC = engine.createComponent(ModelComponent.class);
        TileComponent tileC = engine.createComponent(TileComponent.class);
        ResourceComponent resourceC = engine.createComponent(ResourceComponent.class);

        tileC.hex = hex;
        tileC.bounds = new Polygon(hex.getVertices());

        Vector3 position = new Vector3(
            tileC.bounds.getBoundingRectangle().x,
            tileC.bounds.getBoundingRectangle().y,
            PPM / 2
        );

        ModelBuilder modelBuilder = new ModelBuilder();
        modelC.model = modelBuilder.createSphere(
            PPM * 4, PPM * 4, PPM * 4, 16, 16,
            new Material(ColorAttribute.createDiffuse(Color.GOLD)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        modelC.instance = new ModelInstance(modelC.model);
        modelC.instance.transform.setTranslation(position);

        resourceC.addResource(new EnergyResource(5f));
        resourceC.addResource(new FoodResource(0f));
        resourceC.addResource(new InfluenceResource(0f));
        resourceC.addResource(new MoneyResource(0f));
        resourceC.addResource(new ProductionResource(0f));
        resourceC.addResource(new ScienceResource(0f));

        entity.add(modelC);
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
