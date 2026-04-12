package model;

import strategy.EnemyActionStrategy;

public class Goblin extends Enemy {
    public Goblin(EnemyActionStrategy strategy) {
        super(55, 35, 15, 25, strategy);
    }

    public Goblin() {
        this(new strategy.BasicAttackStrategy());
    }
}