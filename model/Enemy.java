package model;

import action.*;
import strategy.BasicAttackStrategy;
import strategy.EnemyActionStrategy;

public class Enemy extends Combatant {
    private final EnemyActionStrategy strategy;

    public Enemy(String name, int maxHp, int attack, int defense, int speed, EnemyActionStrategy strategy) {
        super(name, maxHp, attack, defense, speed);
        this.strategy = strategy;
    }

    public Enemy(int maxHp, int attack, int defense, int speed, EnemyActionStrategy strategy) {
        this("Enemy", maxHp, attack, defense, speed, strategy);
    }

    public Enemy(String name, int maxHp, int attack, int defense, int speed) {
        this(name, maxHp, attack, defense, speed, new BasicAttackStrategy());
    }

    public Enemy(int maxHp, int attack, int defense, int speed) {
        this(maxHp, attack, defense, speed, new BasicAttackStrategy());
    }

    public Action chooseAction(Combatant player) {
        return strategy.chooseAction(this, player);
    }
}
