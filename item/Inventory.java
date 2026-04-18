package item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {

    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        if (item == null) throw new IllegalArgumentException("null item");
        items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getItemCount() { return items.size(); }
    
}