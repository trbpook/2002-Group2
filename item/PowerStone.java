package item;

import action.Action;
import model.Player;
import java.util.List;
import action.TargetMode;
import model.Combatant;
import action.SpecialSkill;

public final class PowerStone implements Item, Action {
 
    // must be built with resetCooldown = false
    private final SpecialSkill mirroredSkill;
 
    public PowerStone(SpecialSkill mirroredSkill) {
        if (mirroredSkill == null) throw new IllegalArgumentException("mirroredSkill null");
        this.mirroredSkill = mirroredSkill;
    }
 
    @Override
    public String getName() { return "Power Stone"; }
 
    @Override
    public String getDescription() {
        return "Trigger your special skill once for free. Cooldown is not affected.";
    }
 
    @Override
    public String getLabel() { return "Use Power Stone"; }
 
    @Override
    public TargetMode getTargetMode(Combatant actor) {
        return mirroredSkill.getTargetMode(actor);
    }
 
    @Override
    public void execute(Combatant actor, List<Combatant> targets) {
        mirroredSkill.execute(actor, targets);
 
        if (actor instanceof Player player) {
            player.getInventory().removeItem(this);
        }
    }
}

 




