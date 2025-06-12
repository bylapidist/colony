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
    private final IntArray dirtyIndices = new IntArray();

    public MapRenderData getRenderData() {
        return renderData;
    }

    /** Returns the indices of currently selected tiles. */
    public IntArray getSelectedTileIndices() {
        return selectedTileIndices;
    }

    /** Queues a tile index to be refreshed on the next update. */
    public void addDirtyIndex(final int index) {
        dirtyIndices.add(index);
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

        Array<Entity> mapTiles = map.getTiles();
        for (int j = 0; j < dirtyIndices.size; j++) {
            int i = dirtyIndices.get(j);
            Entity entity = mapTiles.get(i);
            TileComponent tc = tileMapper.get(entity);
            ResourceComponent rc = resourceMapper.get(entity);
            if (tc.isDirty()) {
                int index = selectedTileIndices.indexOf(i);
                if (tc.isSelected()) {
                    if (index == -1) {
                        selectedTileIndices.add(i);
                    }
                } else if (index != -1) {
                    selectedTileIndices.removeIndex(index);
                }
                tc.setDirty(false);
            }
            if (rc.isDirty()) {
                rc.setDirty(false);
            }
        }

        if (dirtyIndices.size > 0) {
            MapRenderDataBuilder.updateTiles(map, world, data, dirtyIndices);
        }
        dirtyIndices.clear();

        Array<Entity> mapEntities = map.getEntities();
        Array<RenderBuilding> buildings = data.getBuildings();
        if (mapEntities.size != buildings.size) {
            buildings.clear();
            for (int i = 0; i < mapEntities.size; i++) {
                Entity e = mapEntities.get(i);
                BuildingComponent bc = buildingMapper.get(e);
                buildings.add(MapRenderDataBuilder.toBuilding(bc));
                bc.setDirty(false);
            }
        } else {
            for (int i = 0; i < mapEntities.size; i++) {
                Entity e = mapEntities.get(i);
                BuildingComponent bc = buildingMapper.get(e);
                if (bc.isDirty()) {
                    buildings.set(i, MapRenderDataBuilder.toBuilding(bc));
                    bc.setDirty(false);
                }
            }
        }

        data.setVersion(map.getVersion());
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
