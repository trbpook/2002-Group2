package action;

import java.util.List;
import model.Combatant;

public abstract class SpecialSkill extends Action {
    protected boolean resetCD = true;

    public SpecialSkill(Combatant actor, List<Combatant> targets) {
        super(actor, targets);
    }
    public SpecialSkill(Combatant actor, Combatant target) {
        super(actor, target);
    }

    public SpecialSkill(Combatant actor) {
        super(actor);
    }
}
