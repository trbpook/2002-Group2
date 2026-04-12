package action;

import effect.StatusEffect;
import model.Combatant;

public class ApplyEffectAction extends Action {
    private StatusEffect effect;

    public ApplyEffectAction(Combatant actor, StatusEffect effect) {
        super(actor, actor);
        this.effect = effect;
    }

    public ApplyEffectAction(Combatant actor, Combatant target, StatusEffect effect) {
        super(actor, target);
        this.effect = effect;
    }

    public void executeSingle(Combatant target) {
        target.getEffects().addEffect(effect);
    }

    public String getName() {
        return "Apply Effect: " + effect.getName();
    }

    public TargetMode getDefaultTargetMode() {
        return TargetMode.SELF;
    }
}
