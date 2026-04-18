package item;

import action.Action;
import effect.SmokeBombInvulnerability;
import model.Player;
import java.util.List;
import action.TargetMode;
import model.Combatant;

public final class SmokeBomb implements Item, Action {
 
    private static final int DURATION = 2; // current + next turn
 
    @Override
    public String getName() { return "Smoke Bomb"; }
 
    @Override
    public String getDescription() {
        return "Enemy attacks deal 0 damage for the current and next turn.";
    }
 
    @Override
    public String getLabel() { return "Use Smoke Bomb"; }
 
    @Override
    public TargetMode getTargetMode(Combatant actor) {
        return TargetMode.SELF;
    }
 
    @Override
    public void execute(Combatant actor, List<Combatant> targets) {
        targets.get(0).getEffects().addEffect(new SmokeBombInvulnerability(DURATION));
 
        if (actor instanceof Player player) {
            player.getInventory().removeItem(this);
        }
    }
}