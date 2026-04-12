package model;

import action.*;
import item.Inventory;

public abstract class Player extends Combatant {
    private Cooldown skillCD;
    private Inventory inventory;

    public Player(int maxHp, int attack, int defense, int speed) {
        super(maxHp, attack, defense, speed);
        skillCD = new Cooldown(3); // Example cooldown duration for a special skill
        inventory = new Inventory();
    }

    public Cooldown getSkillCD() {
        return skillCD;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract SpecialSkill createSpecialSkill(boolean resetCD);


    
}
