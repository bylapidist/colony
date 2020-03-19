package net.lapidist.colony.components;

import com.artemis.Component;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent extends Component {

    private List<Integer> items = new ArrayList<>();

    public final List<Integer> getItems() {
        return items;
    }

    public final void addItem(final int item) {
        addItem(items.size(), item);
    }

    public final void addItem(final int slot, final int item) {
        items.add(slot, item);
    }

    public final int getItem(final int slot) {
        return items.get(slot);
    }

    public final void removeItem(final int slot) {
        items.remove(slot);
    }

    public final int size() {
        return items.size();
    }
}
