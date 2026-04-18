package util;

import model.Combatant;

public class DamageCalculator {
    public static int calculate(Combatant attacker, Combatant defender) {
        return Math.max(0, attacker.getAttack() - defender.getDefense());
    }
}
