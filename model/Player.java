package model;

import action.Action;
import action.BasicAttack;
import action.Defend;
import action.SpecialSkill;
import java.util.ArrayList;
import java.util.List;
import item.Inventory;
import item.Item;

public abstract class Player extends Combatant {
    private final Cooldown skillCD;
    private final Inventory inventory;

    public Player(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
        skillCD = new Cooldown(3);
        inventory = new Inventory();
    }

    public Player(int maxHp, int attack, int defense, int speed) {
        this("Player", maxHp, attack, defense, speed);
    }

    public Cooldown getSkillCD() {
        return skillCD;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract SpecialSkill createSpecialSkill(boolean resetCD);

    public List<Action> getAvailableActions() {
        List<Action> availableActions = new ArrayList<>();
        availableActions.add(new BasicAttack());
        availableActions.add(new Defend());

        if (canUseSpecialSkillThisTurn()) {
            availableActions.add(createSpecialSkill(true));
        }

        for (Item item : inventory.getItems()) {
            if (item instanceof Action action) {
                availableActions.add(action);
            }
        }

        return availableActions;
    }

    public boolean canUseSpecialSkillThisTurn() {
        return skillCD.isReady();
    }
}
