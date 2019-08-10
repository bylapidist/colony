package net.lapidist.colony.components;

import com.artemis.Component;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent extends Component {

    private List<Integer> items = new ArrayList<>();

    public List<Integer> getItems() {
        return items;
    }

    public void addItem(int item) {
        addItem(items.size(), item);
    }

    public void addItem(int slot, int item) {
        items.add(slot, item);
    }

    public int getItem(int slot) {
        return items.get(slot);
    }

    public void removeItem(int slot) {
        items.remove(slot);
    }

    public int size() {
        return items.size();
    }
}
