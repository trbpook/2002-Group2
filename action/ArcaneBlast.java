package action;

import java.util.List;
import model.Combatant;
import model.Player;
import effect.ArcaneBlastBuff;
import util.DamageCalculator;

public class ArcaneBlast extends SpecialSkill {
    private static final int ATTACK_BONUS_PER_KILL = 10;

    public ArcaneBlast(Combatant actor, boolean resetCD) {
        super(actor, resetCD);
    }

    public ArcaneBlast(Combatant actor, List<Combatant> targets, boolean resetCD) {
        super(actor, targets, resetCD);
    }

    @Override
    public void execute() {
        // Apply damage to the target
        int attackBonus = 0;
        for (Combatant target : targets) {
            int damage = DamageCalculator.calculateDamage(actor, target); // Example damage calculation
            target.takeDamage(damage);

            if (!target.isAlive()) {
                // If the target is killed, increase the attack bonus for the buff
                attackBonus += ATTACK_BONUS_PER_KILL;
            }
        }

        // Apply Arcane Blast buff to the actor
        ArcaneBlastBuff buff = new ArcaneBlastBuff(attackBonus); // Example attack bonus
        actor.getEffects().addEffect(buff);

        //Reset cooldown if applicable
        if (resetCD) {
            ((Player) actor).getSkillCD().reset();
        }
    }

    @Override
    public void executeSingle(Combatant target) {
        int damage = DamageCalculator.calculateDamage(actor, target); // Example damage calculation
        target.takeDamage(damage);

        if (!target.isAlive()) {
            // If the target is killed, apply the Arcane Blast buff with the appropriate attack bonus
            ArcaneBlastBuff buff = new ArcaneBlastBuff(ATTACK_BONUS_PER_KILL); // Example attack bonus for single kill
            actor.getEffects().addEffect(buff);
        }
    }

    @Override
    public TargetMode getDefaultTargetMode() {
        return TargetMode.ALL_OPPONENTS;
    }

    public String getName() {
        return "Arcane Blast";
    }
}
