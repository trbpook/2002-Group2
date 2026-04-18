package action;

import java.util.List;

import model.Combatant;
import util.DamageCalculator;

public class BasicAttack implements Action {

    @Override
    public String getLabel() {
        return "Basic Attack";
    }

    @Override
    public TargetMode getTargetMode(Combatant actor) {
        return TargetMode.SINGLE_OPPONENT;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets) {
        for (Combatant target : targets) {
            int damage = DamageCalculator.calculate(actor, target);
            target.takeDamage(damage);
        }
    }
}