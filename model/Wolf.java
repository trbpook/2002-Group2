package model;

import strategy.BasicAttackStrategy;
import strategy.EnemyActionStrategy;

public class Wolf extends Enemy {
    public Wolf(String name, EnemyActionStrategy strategy) {
        super(name, 40, 45, 5, 35, strategy);
    }

    public Wolf(String name) {
        this(name, new BasicAttackStrategy());
    }

    public Wolf(EnemyActionStrategy strategy) {
        this("Wolf", strategy);
    }

    public Wolf() {
        this("Wolf");
    }
}
