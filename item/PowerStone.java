package item;

import action.Action;
import model.Player;

public class PowerStone implements Item {
    @Override
    public Action createAction(Player player) {
        return player.createSpecialSkill(false);
    }

    @Override
    public String getName() {
        return "Power Stone";
    }
}
