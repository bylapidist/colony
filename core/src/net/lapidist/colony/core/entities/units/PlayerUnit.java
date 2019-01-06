package net.lapidist.colony.core.entities.units;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.lapidist.colony.core.components.TileComponent;
import net.lapidist.colony.core.components.traits.SpriteTrait;
import net.lapidist.colony.core.entities.UnitType;

import static net.lapidist.colony.core.Constants.resourceLoader;

class PlayerUnit extends Unit {

    PlayerUnit(Unit unit) {
        TileComponent tileC = unit.getEngine().createComponent(TileComponent.class);
        SpriteTrait spriteT = unit.getEngine().createComponent(SpriteTrait.class);

        tileC.tile = unit.getTile();
        tileC.unitType = UnitType.PLAYER;
        TextureRegion texture = resourceLoader.getRegion("noise");
        spriteT.sprite = new Sprite(texture);
        spriteT.sprite.setBounds(
                unit.getTile().getBoundingBox().x,
                unit.getTile().getBoundingBox().y,
                unit.getTile().getBoundingBox().getWidth(),
                unit.getTile().getBoundingBox().getHeight()
        );

        this.add(tileC)
                .add(spriteT);
    }
}
