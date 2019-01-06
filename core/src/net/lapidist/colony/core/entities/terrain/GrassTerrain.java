package net.lapidist.colony.core.entities.terrain;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.components.traits.SpriteTrait;
import net.lapidist.colony.core.entities.TerrainType;

import static net.lapidist.colony.core.Constants.resourceLoader;

class GrassTerrain extends Terrain {

    GrassTerrain(Terrain terrain) {
        TileComponent tileC = terrain.getEngine().createComponent(TileComponent.class);
        SpriteTrait spriteT = terrain.getEngine().createComponent(SpriteTrait.class);

        tileC.tile = terrain.getTile();
        tileC.terrainType = TerrainType.GRASS;
        TextureRegion texture = resourceLoader.getRegion("space");
        spriteT.sprite = new Sprite(texture);
        spriteT.sprite.setBounds(
                terrain.getTile().getBoundingBox().x,
                terrain.getTile().getBoundingBox().y,
                terrain.getTile().getBoundingBox().getWidth(),
                terrain.getTile().getBoundingBox().getHeight()
        );

        this.add(tileC)
                .add(spriteT);
    }
}
