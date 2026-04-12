package item;

import action.Action;
import action.HealAction;
import model.Player;

public class Potion implements Item {
    private static final int HEAL_AMOUNT = 100;

    @Override
    public Action createAction(Player player) {
        return new HealAction(player, player, HEAL_AMOUNT);
    }

    @Override
    public String getName() {
        return "Potion";
    }
}
