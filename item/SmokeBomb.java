package item;

import action.Action;
import action.ApplyEffectAction;
import effect.SmokeBombInvulnerability;
import model.Player;

public class SmokeBomb implements Item {
    @Override
    public Action createAction(Player player) {
        return new ApplyEffectAction(player, player, new SmokeBombInvulnerability());
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }
}
