package engine;

import action.Action;
import action.TargetMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Combatant;
import model.Enemy;
import model.Player;
import strategy.SpeedBasedTurnOrder;
import strategy.TurnOrderStrategy;
import ui.GameUI;

public class BattleEngine {
    public enum BattleResult {
        PLAYER_VICTORY,
        PLAYER_DEFEAT
    }

    private final Player player;
    private final Level level;
    private final List<Enemy> enemies;
    private final TurnOrderStrategy turnOrderStrategy;
    private final GameUI gameUI;
    private final int initialEnemyCount;
    private boolean backupTriggered;
    private int roundNumber;

    public BattleEngine(Player player, Level level, GameUI gameUI) {
        this(player, level, new SpeedBasedTurnOrder(), gameUI);
    }

    public BattleEngine(Player player, Level level, TurnOrderStrategy turnOrderStrategy, GameUI gameUI) {
        this.player = player;
        this.level = level;
        this.turnOrderStrategy = turnOrderStrategy;
        this.gameUI = gameUI;
        this.enemies = new ArrayList<>(level.createInitialEnemies());
        this.initialEnemyCount = enemies.size();
        this.backupTriggered = false;
        this.roundNumber = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    public List<Enemy> getAliveEnemies() {
        List<Enemy> aliveEnemies = new ArrayList<>();

        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                aliveEnemies.add(enemy);
            }
        }

        return aliveEnemies;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public BattleResult startBattle() {
        gameUI.displayLoadingScreen();
        gameUI.displayBattleState(player, enemies);

        while (!isBattleOver()) {
            runRound();
        }

        BattleResult result = player.isAlive() ? BattleResult.PLAYER_VICTORY : BattleResult.PLAYER_DEFEAT;
        if (result == BattleResult.PLAYER_VICTORY) {
            gameUI.displayVictory(player, roundNumber);
        } else {
            gameUI.displayDefeat(enemies, roundNumber);
        }

        return result;
    }

    public void runRound() {
        triggerBackupSpawnIfNeeded();
        if (isBattleOver()) {
            return;
        }

        roundNumber++;
        List<Combatant> turnOrder = turnOrderStrategy.determineTurnOrder(getAllCombatants());
        gameUI.displayRoundInfo(roundNumber, turnOrder);

        for (Combatant combatant : turnOrder) {
            if (!combatant.isAlive()) {
                continue;
            }

            processTurn(combatant);
            triggerBackupSpawnIfNeeded();
            gameUI.displayBattleState(player, enemies);

            if (isBattleOver()) {
                return;
            }
        }

        tickEndOfRound();
    }

    public void processTurn(Combatant combatant) {
        if (!combatant.isAlive()) {
            return;
        }

        if (combatant.getEffects().preventsAction()) {
            gameUI.displaySkippedTurn(combatant);
            if (combatant == player) {
                advancePlayerCooldown();
            }
            return;
        }

        Action action;
        if (combatant == player) {
            action = resolvePlayerAction(player);
        } else if (combatant instanceof Enemy enemy) {
            action = enemy.chooseAction(player);
        } else {
            throw new IllegalStateException("Unsupported combatant type: " + combatant.getClass().getName());
        }

        if (action == null) {
            return;
        }

        List<Combatant> targets = resolveTargets(action.getTargetMode(combatant), combatant);
        gameUI.displayTurnInfo(combatant, action, targets);
        action.execute(combatant, targets);

        if (combatant == player) {
            advancePlayerCooldown();
        }
    }

    public Action resolvePlayerAction(Player actingPlayer) {
        List<Action> availableActions = actingPlayer.getAvailableActions();
        int actionIndex = gameUI.promptActionSelection(availableActions);
        return availableActions.get(actionIndex);
    }

    public List<Combatant> resolveTargets(TargetMode mode, Combatant actor) {
        return switch (mode) {
            case SELF -> List.of(actor);
            case SINGLE_OPPONENT -> resolveSingleOpponentTargets(actor);
            case ALL_OPPONENTS -> resolveAllOpponentTargets(actor);
        };
    }

    private List<Combatant> resolveSingleOpponentTargets(Combatant actor) {
        if (actor == player) {
            List<Enemy> aliveEnemies = getAliveEnemies();
            if (aliveEnemies.isEmpty()) {
                return List.of();
            }

            int targetIndex = gameUI.promptTargetSelection(aliveEnemies);
            return List.of(aliveEnemies.get(targetIndex));
        }

        return List.of(player);
    }

    private List<Combatant> resolveAllOpponentTargets(Combatant actor) {
        if (actor == player) {
            return new ArrayList<>(getAliveEnemies());
        }

        return List.of(player);
    }

    private List<Combatant> getAllCombatants() {
        List<Combatant> combatants = new ArrayList<>();
        combatants.add(player);
        combatants.addAll(enemies);
        return combatants;
    }

    private void advancePlayerCooldown() {
        player.getSkillCD().tick();
    }

    private void tickEndOfRound() {
        player.getEffects().tick();

        for (Enemy enemy : enemies) {
            enemy.getEffects().tick();
        }
    }

    private boolean isBattleOver() {
        if (!player.isAlive()) {
            return true;
        }

        if (!getAliveEnemies().isEmpty()) {
            return false;
        }

        return backupTriggered || !level.hasBackupSpawn();
    }

    private void triggerBackupSpawnIfNeeded() {
        if (backupTriggered || !level.hasBackupSpawn() || !areInitialEnemiesDefeated()) {
            return;
        }

        List<Enemy> backupEnemies = level.createBackupEnemies();
        enemies.addAll(backupEnemies);
        backupTriggered = true;
        gameUI.displayBackupSpawn(backupEnemies);
    }

    private boolean areInitialEnemiesDefeated() {
        for (int i = 0; i < initialEnemyCount; i++) {
            if (enemies.get(i).isAlive()) {
                return false;
            }
        }

        return true;
    }
}
