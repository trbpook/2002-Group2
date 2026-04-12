package action;

import effect.StunEffect;
import util.DamageCalculator;
import model.Combatant;
import model.Player;

public class ShieldBash extends SpecialSkill {
    public ShieldBash(Combatant actor, Combatant target, boolean resetCD) {
        super(actor, target, resetCD);
    }

    public ShieldBash(Combatant actor, boolean resetCD) {
        super(actor, resetCD);
    }

    @Override
    public void executeSingle(Combatant target) {
        //Deal BasicAttack damage
        int damage = DamageCalculator.calculateDamage(actor, target);
        target.takeDamage(damage);

        //Apply Stun for 2 turns (by default)
        target.getEffects().addEffect(new StunEffect());
    }

    @Override
    public void execute() {
        for (Combatant target : targets) {
            executeSingle(target);
        }

        if (resetCD) {
            ((Player) actor).getSkillCD().reset();
        }
    }
    
    public String getName() {
        return "Shield Bash";
    }

    public TargetMode getDefaultTargetMode() {
        return TargetMode.SELECT_SINGLE_OPPONENT;
    }
}
