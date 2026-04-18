package action;

import java.util.List;

import model.Combatant;
import util.DamageCalculator;

public class ArcaneBlast extends SpecialSkill {

    public ArcaneBlast(boolean resetCooldown) {
        super(resetCooldown);
    }

    @Override
    public String getLabel() {
        return "Arcane Blast";
    }

    @Override
    public TargetMode getTargetMode(Combatant actor) {
        return TargetMode.ALL_OPPONENTS;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets) {
        for (Combatant target : targets) {
            int damage = DamageCalculator.calculate(actor, target);
            target.takeDamage(damage);
        }
        applyCooldown(actor);
    }
}