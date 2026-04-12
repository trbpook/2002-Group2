package item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
    private final List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public Item removeItem(int index) {
        return items.remove(index);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void clear() {
        items.clear();
    }
}
