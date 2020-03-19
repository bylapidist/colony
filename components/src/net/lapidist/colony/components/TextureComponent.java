package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;

public class TextureComponent extends Component {

    private Texture texture;

    public final Texture getTexture() {
        return texture;
    }

    public final void setTexture(final Texture textureToSet) {
        this.texture = textureToSet;
    }
}
