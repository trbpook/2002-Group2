package model;

import strategy.BasicAttackStrategy;
import strategy.EnemyActionStrategy;

public class Wolf extends Enemy {
    public Wolf(EnemyActionStrategy strategy) {
        super(40, 45, 5, 35, strategy);
    }

    public Wolf() {
        this(new BasicAttackStrategy());
    }
}
