package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontComponent extends Component {

    private BitmapFont font;

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }
}
