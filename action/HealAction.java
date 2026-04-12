package action;

import model.Combatant;

public class HealAction extends Action {
    private static final int DEFAULT_HEAL_AMOUNT = 100;
    private int healAmount;

    public HealAction(Combatant actor, Combatant target) {
        this(actor, target, DEFAULT_HEAL_AMOUNT);
    }

    public HealAction(Combatant actor, Combatant target, int healAmount) {
        super(actor, target);
        this.healAmount = healAmount;
    }

    public HealAction(Combatant actor) {
        this(actor, actor, DEFAULT_HEAL_AMOUNT);
    }

    public HealAction(Combatant actor, int healAmount) {
        this(actor, actor, healAmount);
    }

    @Override
    public void executeSingle(Combatant target) {
        target.heal(healAmount);
    }

    public String getName() {
        return "Heal";
    }

    public TargetMode getDefaultTargetMode() {
        return TargetMode.SELF;
    }

}
