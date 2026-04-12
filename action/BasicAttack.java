package action;

import model.Combatant;
import util.DamageCalculator;

public class BasicAttack extends Action {
    public BasicAttack(Combatant actor, Combatant target) {
        super(actor, target);
    }

    public BasicAttack(Combatant actor) {
        super(actor);
    }

    @Override
    public TargetMode getDefaultTargetMode() {
        return TargetMode.SELECT_SINGLE_OPPONENT;
    }

    @Override
    public void executeSingle(Combatant target) {
        int damage = DamageCalculator.calculateDamage(actor, target);
        target.takeDamage(damage);
    }
    
}
