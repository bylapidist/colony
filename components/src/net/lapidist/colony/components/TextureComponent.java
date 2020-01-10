package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;

public class TextureComponent extends Component {

    private Texture texture;

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
