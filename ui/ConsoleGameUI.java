package ui;

import action.Action;
import action.ArcaneBlast;
import action.BasicAttack;
import action.Defend;
import action.ShieldBash;
import engine.BattleEngine;
import engine.Level;
import item.Item;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Combatant;
import model.Enemy;
import model.Player;

public final class ConsoleGameUI implements GameUI {
    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleGameUI() {
        this(System.in, System.out);
    }

    public ConsoleGameUI(InputStream inputStream, PrintStream outputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null.");
        }

        this.scanner = new Scanner(inputStream);
        this.out = outputStream;
    }

    public void displayWelcomeScreen() {
        out.println();
        out.println("=== Turn-Based Combat ===");
        out.println("Build your player, pick two starting items, and survive the enemy wave.");
        out.println();
    }

    public GameConfiguration promptNewGameConfiguration() {
        PlayerClassOption playerClass = promptPlayerClassSelection();
        List<StartingItemType> startingItems = promptStartingItems(playerClass);
        Level level = promptLevelSelection();
        GameConfiguration configuration = new GameConfiguration(playerClass, startingItems, level);

        displayConfigurationSummary(configuration);
        return configuration;
    }

    public PostBattleChoice promptPostBattleChoice(BattleEngine.BattleResult result) {
        out.println();
        if (result == BattleEngine.BattleResult.PLAYER_VICTORY) {
            out.println("What next?");
        } else {
            out.println("Choose what to do next.");
        }

        List<String> options = new ArrayList<>();
        for (PostBattleChoice choice : PostBattleChoice.values()) {
            options.add(choice.getDisplayName());
        }

        return PostBattleChoice.values()[promptMenuChoice("Post-Battle Menu", options)];
    }

    public void displayGoodbye() {
        out.println();
        out.println("Exiting game.");
    }

    @Override
    public void displayLoadingScreen() {
        out.println();
        out.println("Starting battle...");
    }

    @Override
    public int promptActionSelection(List<Action> actions) {
        List<String> options = new ArrayList<>();

        for (Action action : actions) {
            options.add(action.getLabel() + actionDetailSuffix(action));
        }

        return promptMenuChoice("Choose Your Action", options);
    }

    @Override
    public int promptTargetSelection(List<? extends Combatant> targets) {
        if (targets.size() == 1) {
            Combatant onlyTarget = targets.get(0);
            out.println("Target locked: " + formatCombatantSummary(onlyTarget));
            return 0;
        }

        List<String> options = new ArrayList<>();
        for (Combatant target : targets) {
            options.add(formatCombatantSummary(target));
        }

        return promptMenuChoice("Choose Target", options);
    }

    @Override
    public void displayRoundInfo(int roundNumber, List<Combatant> turnOrder) {
        out.println();
        out.println("=== Round " + roundNumber + " ===");
        out.println("Turn Order: " + turnOrder.stream().map(Combatant::getName).toList());
    }

    @Override
    public void displayTurnInfo(Combatant combatant, Action action, List<Combatant> targets) {
        out.println();
        out.println(combatant.getName() + " uses " + action.getLabel() + " on " + formatTargetList(targets) + ".");
    }

    @Override
    public void displaySkippedTurn(Combatant combatant) {
        out.println();
        out.println(combatant.getName() + " cannot act this turn.");
    }

    @Override
    public void displayBackupSpawn(List<Enemy> backupEnemies) {
        out.println();
        out.println("Backup enemies have arrived: " + backupEnemies.stream().map(Enemy::getName).toList());
    }

    @Override
    public void displayBattleState(Player player, List<Enemy> enemies) {
        out.println();
        out.println("--- Battle State ---");
        out.println("Player: " + formatCombatantSummary(player)
            + " | Skill Cooldown: " + formatCooldown(player));
        for (Enemy enemy : enemies) {
            out.println("Enemy:  " + formatCombatantSummary(enemy));
        }
    }

    @Override
    public void displayVictory(Player player, int rounds) {
        out.println();
        out.println("Victory in " + rounds + " round(s). " + player.getName() + " survives with " + player.getHp() + " HP.");
    }

    @Override
    public void displayDefeat(List<Enemy> enemies, int rounds) {
        out.println();
        out.println("Defeat after " + rounds + " round(s). Remaining enemies: " + enemies.stream()
            .filter(Enemy::isAlive)
            .map(Enemy::getName)
            .toList());
    }

    private PlayerClassOption promptPlayerClassSelection() {
        List<String> options = new ArrayList<>();

        for (PlayerClassOption playerClass : PlayerClassOption.values()) {
            options.add(playerClass.getDisplayName() + " | " + playerClass.getStatsSummary()
                + " | " + playerClass.getSpecialSkillSummary());
        }

        int index = promptMenuChoice("Choose Your Class", options);
        return PlayerClassOption.values()[index];
    }

    private List<StartingItemType> promptStartingItems(PlayerClassOption playerClass) {
        Player previewPlayer = playerClass.createPlayer();
        List<StartingItemType> selectedItems = new ArrayList<>();

        for (int slot = 1; slot <= 2; slot++) {
            List<String> options = new ArrayList<>();
            for (StartingItemType itemType : StartingItemType.values()) {
                options.add(itemType.getDisplayName(previewPlayer) + " | " + itemType.getDescription(previewPlayer));
            }

            int index = promptMenuChoice("Choose Starting Item " + slot + " of 2", options);
            selectedItems.add(StartingItemType.values()[index]);
        }

        return selectedItems;
    }

    private Level promptLevelSelection() {
        List<String> options = new ArrayList<>();

        for (Level level : Level.values()) {
            options.add(level.getDisplayName());
        }

        int index = promptMenuChoice("Choose Difficulty", options);
        return Level.values()[index];
    }

    private void displayConfigurationSummary(GameConfiguration configuration) {
        out.println();
        out.println("Selected Configuration");
        out.println("Class: " + configuration.getPlayerClass().getDisplayName());
        out.println("Items: " + configuration.getStartingItemNames());
        out.println("Difficulty: " + configuration.getLevel().getDisplayName());
    }

    private int promptMenuChoice(String title, List<String> options) {
        while (true) {
            out.println();
            out.println(title);
            for (int i = 0; i < options.size(); i++) {
                out.println((i + 1) + ". " + options.get(i));
            }
            out.print("> ");

            String input = scanner.nextLine().trim();
            try {
                int numericChoice = Integer.parseInt(input);
                if (numericChoice >= 1 && numericChoice <= options.size()) {
                    return numericChoice - 1;
                }
            } catch (NumberFormatException ignored) {
            }

            out.println("Enter a number between 1 and " + options.size() + ".");
        }
    }

    private String actionDetailSuffix(Action action) {
        if (action instanceof Item item) {
            return " | " + item.getDescription();
        }
        if (action instanceof BasicAttack) {
            return " | Deal max(0, ATK - DEF) damage to one enemy.";
        }
        if (action instanceof Defend) {
            return " | Gain +10 DEF for the current and next round.";
        }
        if (action instanceof ShieldBash) {
            return " | Deal attack damage and stun one enemy.";
        }
        if (action instanceof ArcaneBlast) {
            return " | Deal attack damage to all enemies and gain +10 ATK per kill.";
        }

        return "";
    }

    private String formatCombatantSummary(Combatant combatant) {
        String status = combatant.isAlive() ? combatant.getHp() + "/" + combatant.getMaxHp() + " HP" : "DEFEATED";
        String effects = combatant.getEffects().getActiveEffectNames().isEmpty()
            ? "none"
            : combatant.getEffects().getActiveEffectNames().toString();
        return combatant.getName() + " (" + status
            + ", ATK " + combatant.getAttack()
            + ", DEF " + combatant.getDefense()
            + ", SPD " + combatant.getSpeed()
            + ", Effects: " + effects + ")";
    }

    private String formatCooldown(Player player) {
        int remaining = player.getSkillCD().getRemaining();
        return remaining == 0 ? "Ready" : remaining + " turn(s) remaining";
    }

    private String formatTargetList(List<Combatant> targets) {
        if (targets.isEmpty()) {
            return "no targets";
        }

        return targets.stream().map(Combatant::getName).toList().toString();
    }
}
