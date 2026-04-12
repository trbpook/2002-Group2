package model;

import action.*;
import strategy.BasicAttackStrategy;
import strategy.EnemyActionStrategy;

public class Enemy extends Combatant {
    EnemyActionStrategy strategy;

    public Enemy(int maxHp, int attack, int defense, int speed, EnemyActionStrategy strategy) {
        super(maxHp, attack, defense, speed);
        this.strategy = strategy;
    }

    public Enemy(int maxHp, int attack, int defense, int speed) {
        this(maxHp, attack, defense, speed, new BasicAttackStrategy());
    }

    Action chooseAction(Combatant player) {
        return strategy.chooseAction(this, player);
    }
}
