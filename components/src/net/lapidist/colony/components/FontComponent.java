package net.lapidist.colony.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontComponent extends Component {

    private BitmapFont font;

    public final BitmapFont getFont() {
        return font;
    }

    public final void setFont(final BitmapFont fontToSet) {
        this.font = fontToSet;
    }
}
