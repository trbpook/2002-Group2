package item;

import action.Action;
import model.Player;
import java.util.List;
import action.TargetMode;
import model.Combatant;

public final class Potion implements Item, Action {
 
    private static final int HEAL_AMOUNT = 100;
 
    @Override
    public String getName() { return "Potion"; }
 
    @Override
    public String getDescription() {
        return "Heal " + HEAL_AMOUNT + " HP.";
    }
 
    @Override
    public String getLabel() { return "Use Potion"; }
 
    @Override
    public TargetMode getTargetMode(Combatant actor) {
        return TargetMode.SELF;
    }
 
    @Override
    public void execute(Combatant actor, List<Combatant> targets) {
        // SELF target -> engine passes [actor]
        targets.get(0).heal(HEAL_AMOUNT);
 
        if (actor instanceof Player player) {
            player.getInventory().removeItem(this);
        }
    }
}
 





