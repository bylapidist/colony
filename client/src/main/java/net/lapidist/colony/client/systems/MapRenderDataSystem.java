package net.lapidist.colony.client.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import net.lapidist.colony.client.render.MapRenderData;
import net.lapidist.colony.client.render.MapRenderDataBuilder;
import net.lapidist.colony.client.render.SimpleMapRenderData;
import net.lapidist.colony.client.render.data.RenderBuilding;
import net.lapidist.colony.client.render.data.RenderTile;
import net.lapidist.colony.components.maps.MapComponent;
import net.lapidist.colony.components.maps.TileComponent;
import net.lapidist.colony.components.resources.ResourceComponent;
import net.lapidist.colony.components.entities.BuildingComponent;
import net.lapidist.colony.map.MapUtils;

/**
 * Maintains the {@link MapRenderData} used for rendering.
 */
public final class MapRenderDataSystem extends BaseSystem {
    private MapRenderData renderData;
    private MapComponent map;
    private int lastVersion;
    private ComponentMapper<TileComponent> tileMapper;
    private ComponentMapper<ResourceComponent> resourceMapper;
    private ComponentMapper<BuildingComponent> buildingMapper;
    private final IntArray selectedTileIndices = new IntArray();

    public MapRenderData getRenderData() {
        return renderData;
    }

    /** Returns the indices of currently selected tiles. */
    public IntArray getSelectedTileIndices() {
        return selectedTileIndices;
    }

    @Override
    public void initialize() {
        tileMapper = world.getMapper(TileComponent.class);
        resourceMapper = world.getMapper(ResourceComponent.class);
        buildingMapper = world.getMapper(BuildingComponent.class);
        map = MapUtils.findMap(world).orElse(null);
        if (map != null) {
            renderData = MapRenderDataBuilder.fromMap(map, world);
            lastVersion = map.getVersion();
            rebuildSelectedIndices();
        }
    }

    @Override
    protected void processSystem() {
        if (map == null) {
            map = MapUtils.findMap(world).orElse(null);
            if (map == null) {
                return;
            }
        }
        if (renderData == null) {
            renderData = MapRenderDataBuilder.fromMap(map, world);
            lastVersion = map.getVersion();
            rebuildSelectedIndices();
            return;
        }
        if (map.getVersion() != lastVersion) {
            updateIncremental();
            lastVersion = map.getVersion();
        }
    }

    private void updateIncremental() {
        SimpleMapRenderData data = (SimpleMapRenderData) renderData;

        IntArray modified = new IntArray();
        Array<Entity> mapTiles = map.getTiles();
        selectedTileIndices.clear();
        for (int i = 0; i < mapTiles.size; i++) {
            Entity entity = mapTiles.get(i);
            TileComponent tc = tileMapper.get(entity);
            ResourceComponent rc = resourceMapper.get(entity);
            RenderTile old = data.getTiles().get(i);
            if (old == null || tileChanged(old, tc, rc)) {
                modified.add(i);
            }
            if (tc.isSelected()) {
                selectedTileIndices.add(i);
            }
        }

        if (modified.size > 0) {
            MapRenderDataBuilder.updateTiles(map, world, data, modified);
        }

        Array<Entity> mapEntities = map.getEntities();
        Array<RenderBuilding> buildings = data.getBuildings();
        if (mapEntities.size != buildings.size) {
            buildings.clear();
            for (int i = 0; i < mapEntities.size; i++) {
                Entity e = mapEntities.get(i);
                BuildingComponent bc = buildingMapper.get(e);
                buildings.add(MapRenderDataBuilder.toBuilding(bc));
            }
        } else {
            for (int i = 0; i < mapEntities.size; i++) {
                Entity e = mapEntities.get(i);
                BuildingComponent bc = buildingMapper.get(e);
                RenderBuilding old = buildings.get(i);
                if (old == null || buildingChanged(old, bc)) {
                    buildings.set(i, MapRenderDataBuilder.toBuilding(bc));
                }
            }
        }

        data.setVersion(map.getVersion());
    }

    private static boolean tileChanged(final RenderTile old, final TileComponent tc, final ResourceComponent rc) {
        return old.getX() != tc.getX()
                || old.getY() != tc.getY()
                || !old.getTileType().equals(tc.getTileType().toString())
                || old.isSelected() != tc.isSelected()
                || old.getWood() != rc.getWood()
                || old.getStone() != rc.getStone()
                || old.getFood() != rc.getFood();
    }

    private static boolean buildingChanged(final RenderBuilding old, final BuildingComponent bc) {
        return old.getX() != bc.getX()
                || old.getY() != bc.getY()
                || !old.getBuildingType().equals(bc.getBuildingType().toString());
    }

    private void rebuildSelectedIndices() {
        if (map == null) {
            selectedTileIndices.clear();
            return;
        }
        selectedTileIndices.clear();
        Array<Entity> mapTiles = map.getTiles();
        for (int i = 0; i < mapTiles.size; i++) {
            TileComponent tc = tileMapper.get(mapTiles.get(i));
            if (tc.isSelected()) {
                selectedTileIndices.add(i);
            }
        }
    }
}
