package model;

import effect.StatusEffectManager;

public class Combatant {
    protected int maxHp;
    protected int hp;
    protected int attack;
    protected int defense;
    protected int speed;

    StatusEffectManager effects;

    public Combatant(int maxHp, int attack, int defense, int speed) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.effects = new StatusEffectManager();
    }
    
    public Combatant() {
        this.effects = new StatusEffectManager();
    }

    public void takeDamage(int damage) {
        //Take direct damage
        if (effects.isInvulnerable()) {
            return; // No damage taken if invulnerable
        }
        hp = Math.max(0, hp - damage);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public StatusEffectManager getEffects() {
        return effects;
    }

    public int getBaseAttack() {
        return attack;
    }

    public int getBaseDefense() {
        return defense;
    }

    public int getBonusAttack() {
        return effects.getTotalAttackBonus();
    }

    public int getBonusDefense() {
        return effects.getTotalDefenseBonus();
    }

    public int getAttack() {
        return attack + getBonusAttack();
    }

    public int getDefense() {
        return defense + getBonusDefense();
    }

    public int getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
}
