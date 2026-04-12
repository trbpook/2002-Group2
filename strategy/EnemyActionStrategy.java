package strategy;

import action.*;
import model.*;

public interface EnemyActionStrategy {
    public Action chooseAction(Enemy enemy, Combatant player);
}
