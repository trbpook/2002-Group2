package model;

import action.*;

public abstract class Player extends Combatant {
    private Cooldown skillCD;
    //private Inventory inventory; // Placeholder for future inventory system

    public Player(int maxHp, int attack, int defense, int speed) {
        super(maxHp, attack, defense, speed);
        skillCD = new Cooldown(3); // Example cooldown duration for a special skill
    }

    public Cooldown getSkillCD() {
        return skillCD;
    }

    // public Inventory getInventory() {
    //     return inventory; // Placeholder for future inventory system
    // }

    public abstract SpecialSkill createSpecialSkill();


    
}
