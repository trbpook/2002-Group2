package action;

import java.util.List;
import model.Combatant;
import model.Player;

public abstract class SpecialSkill extends Action {
    protected boolean resetCD = true;

    public SpecialSkill(Combatant actor, List<Combatant> targets, boolean resetCD) {
        super(actor, targets);

        if (!(actor instanceof Player)) {
            throw new IllegalArgumentException("SpecialSkill can only be used by Player");
        }
        this.resetCD = resetCD;
    }
    public SpecialSkill(Combatant actor, Combatant target, boolean resetCD) {
        this(actor, wrapTarget(target), resetCD);
    }

    public SpecialSkill(Combatant actor, boolean resetCD) {
        this(actor, (Combatant) null, resetCD);
    }
}
