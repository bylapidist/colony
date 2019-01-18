package net.lapidist.colony.components.render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent extends Component {

    private Sprite sprite;

    public SpriteComponent() {
        this.sprite = new Sprite();
    }

    public SpriteComponent(Sprite sprite) {
        this();

        setSprite(sprite);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
