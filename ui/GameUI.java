package ui;

import action.Action;
import java.util.List;
import model.Combatant;
import model.Enemy;
import model.Player;

public interface GameUI {
    void displayLoadingScreen();

    int promptActionSelection(List<Action> actions);

    int promptTargetSelection(List<? extends Combatant> targets);

    void displayRoundInfo(int roundNumber, List<Combatant> turnOrder);

    void displayTurnInfo(Combatant combatant, Action action, List<Combatant> targets);

    void displaySkippedTurn(Combatant combatant);

    void displayBackupSpawn(List<Enemy> backupEnemies);

    void displayBattleState(Player player, List<Enemy> enemies);

    void displayVictory(Player player, int rounds);

    void displayDefeat(List<Enemy> enemies, int rounds);
}
