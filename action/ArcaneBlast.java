package action;

import effect.ArcaneBlastBuff;
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
        int defeatedTargets = 0;

        for (Combatant target : targets) {
            boolean wasAlive = target.isAlive();
            int damage = DamageCalculator.calculate(actor, target);
            target.takeDamage(damage);
            if (wasAlive && !target.isAlive()) {
                defeatedTargets++;
            }
        }

        if (defeatedTargets > 0) {
            actor.getEffects().addEffect(new ArcaneBlastBuff(defeatedTargets * 10));
        }

        applyCooldown(actor);
    }
}
