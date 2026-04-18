package strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Combatant;

public class SpeedBasedTurnOrder implements TurnOrderStrategy {
    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        List<Combatant> sortedCombatants = new ArrayList<>();

        for (Combatant combatant : combatants) {
            if (combatant.isAlive()) {
                sortedCombatants.add(combatant);
            }
        }

        sortedCombatants.sort(Comparator.comparingInt(Combatant::getSpeed).reversed());
        return sortedCombatants;
    }
}
