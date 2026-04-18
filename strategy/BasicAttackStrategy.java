package strategy;

import action.*;
import model.*;

public class BasicAttackStrategy implements EnemyActionStrategy {
    @Override
    public Action chooseAction(Enemy enemy, Combatant player) {
        return new BasicAttack();
    }
}
