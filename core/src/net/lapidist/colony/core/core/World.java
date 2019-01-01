package net.lapidist.colony.core.core;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import net.lapidist.colony.common.map.MapBuilder;
import net.lapidist.colony.common.map.MapLayout;
import net.lapidist.colony.common.map.tile.ITile;
import net.lapidist.colony.common.map.tile.ITileGrid;
import net.lapidist.colony.core.components.DecalComponent;
import net.lapidist.colony.core.components.ModelComponent;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.events.EventType.WorldInitEvent;
import net.lapidist.colony.common.events.Events;
import net.lapidist.colony.common.modules.Module;
import net.lapidist.colony.core.systems.DebugRenderingSystem;
import net.lapidist.colony.core.systems.MapRenderingSystem;
import net.lapidist.colony.core.systems.PlayerSystem;
import net.lapidist.colony.core.systems.RenderingSystem;

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
        MapBuilder builder = new MapBuilder()
                .setGridHeight(12)
                .setGridWidth(12)
                .setTileWidth(PPM)
                .setTileHeight(PPM)
                .setMapLayout(MapLayout.RECTANGULAR);

        ITileGrid grid = builder.build();
        Iterable<ITile> tiles = grid.getTiles();

        tiles.forEach(tile -> {
            Entity tileEntity = createTile(tile);
            Entity cubeEntity = createCube(tile);

            engine.addEntity(tileEntity);
            engine.addEntity(cubeEntity);
        });
    }

    private Entity createTile(ITile tile) {
        Entity entity = engine.createEntity();
        TextureRegion texture = resourceLoader.getRegion("space");

        DecalComponent decalC = engine.createComponent(DecalComponent.class);
        TileComponent tileC = engine.createComponent(TileComponent.class);

        tileC.tile = tile;
        decalC.decal = Decal.newDecal(texture, true);
        decalC.decal.setPosition(
            tile.getBoundingBox().x,
            tile.getBoundingBox().y,
            -PPM / 2f
        );
        decalC.decal.setDimensions(
            tile.getBoundingBox().getWidth(),
            tile.getBoundingBox().getHeight()
        );

        entity.add(tileC);
        entity.add(decalC);

        return entity;
    }

    private Entity createCube(ITile tile) {
        Entity entity = engine.createEntity();
        Model model = resourceLoader.getModel("cube");

        ModelComponent modelC = engine.createComponent(ModelComponent.class);
        TileComponent tileC = engine.createComponent(TileComponent.class);

        Material material = new Material();
        material.set(
            new BlendingAttribute(true, 1.0f),
            new ColorAttribute(ColorAttribute.Diffuse, 0, 1, 0, 1f),
            new DepthTestAttribute(GL20.GL_LEQUAL, 1f, 8000f, true)
        );

        tileC.tile = tile;
        modelC.instance = new ModelInstance(model, new Vector3(tile.getBoundingBox().x, tile.getBoundingBox().y, 0));

        modelC.instance.materials.get(0).set(material);
        modelC.instance.transform.scl(PPM / 2f);

        entity.add(modelC);
        entity.add(tileC);

        return entity;
    }
}
