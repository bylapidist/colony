package net.lapidist.colony.components;

import com.artemis.Component;

public class LabelComponent extends Component {

    private String text;

    public final String getText() {
        return text;
    }

    public final void setText(final String textToSet) {
        this.text = textToSet;
    }
}
