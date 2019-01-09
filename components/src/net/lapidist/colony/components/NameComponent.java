package net.lapidist.colony.components;

import com.artemis.Component;

public class NameComponent extends Component {

    private String name;

    public NameComponent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
