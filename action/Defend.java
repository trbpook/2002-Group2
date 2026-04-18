package action;

import java.util.List;

import effect.DefendEffect;
import model.Combatant;

public class Defend implements Action {

    @Override
    public String getLabel() {
        return "Defend";
    }

    @Override
    public TargetMode getTargetMode(Combatant actor) {
        return TargetMode.SELF;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets) {
        actor.getEffects().addEffect(new DefendEffect());
    }
}