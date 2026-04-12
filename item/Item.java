package item;

import action.Action;
import model.Player;

public interface Item {
    Action createAction(Player player);
    String getName();
}
