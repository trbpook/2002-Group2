package util;

import model.Combatant;

public class DamageCalculator {
    static public int calculateDamage(Combatant attacker, Combatant defender) {
        return Math.max(0, attacker.getAttack() - defender.getDefense());
    }
}
