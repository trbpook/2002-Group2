package action;

import model.Combatant;
import effect.DefendEffect;

public class Defend extends Action {

    public Defend(Combatant actor) {
        super(actor, actor);
    }

    public Defend(Combatant actor, Combatant target) {
        super(actor, target);
    }

    public void executeSingle(Combatant target) {
        target.getEffects().addEffect(new DefendEffect());
    }

    public String getName() {
        return "Defend";
    }

    public TargetMode getDefaultTargetMode() {
        return TargetMode.SELF;
    }
}
