package net.lapidist.colony.components.gui;

import com.artemis.Component;

public class LabelComponent extends Component {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
