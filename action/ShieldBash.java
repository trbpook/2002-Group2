package action;

import java.util.List;

import effect.StunEffect;
import model.Combatant;
import util.DamageCalculator;

public class ShieldBash extends SpecialSkill {

    public ShieldBash(boolean resetCooldown) {
        super(resetCooldown);
    }

    @Override
    public String getLabel() {
        return "Shield Bash";
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
            target.getEffects().addEffect(new StunEffect());
        }
        applyCooldown(actor);
    }
}