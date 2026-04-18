package action;

import java.util.List;

import model.Combatant;
import model.Player;

public abstract class SpecialSkill implements Action {

    private boolean resetCooldown;

    public SpecialSkill(boolean resetCooldown) {
        this.resetCooldown = resetCooldown;
    }

    protected void applyCooldown(Combatant actor) {
        if (resetCooldown && actor instanceof Player) {
            ((Player) actor).getSkillCD().reset();
        }
    }
}