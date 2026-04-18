package model;

import strategy.EnemyActionStrategy;

public class Goblin extends Enemy {
    public Goblin(String name, EnemyActionStrategy strategy) {
        super(name, 55, 35, 15, 25, strategy);
    }

    public Goblin(String name) {
        this(name, new strategy.BasicAttackStrategy());
    }

    public Goblin(EnemyActionStrategy strategy) {
        this("Goblin", strategy);
    }

    public Goblin() {
        this("Goblin");
    }
}
