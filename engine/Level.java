package engine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import model.Enemy;

public enum Level {
    EASY(
        new EnemyType[] {EnemyType.GOBLIN, EnemyType.GOBLIN, EnemyType.GOBLIN},
        new EnemyType[0]
    ),
    MEDIUM(
        new EnemyType[] {EnemyType.GOBLIN, EnemyType.WOLF},
        new EnemyType[] {EnemyType.WOLF, EnemyType.WOLF}
    ),
    HARD(
        new EnemyType[] {EnemyType.GOBLIN, EnemyType.GOBLIN},
        new EnemyType[] {EnemyType.GOBLIN, EnemyType.WOLF, EnemyType.WOLF}
    );

    private final EnemyType[] initialSpawn;
    private final EnemyType[] backupSpawn;

    Level(EnemyType[] initialSpawn, EnemyType[] backupSpawn) {
        this.initialSpawn = initialSpawn;
        this.backupSpawn = backupSpawn;
    }

    public List<Enemy> createInitialEnemies() {
        return createEnemies(initialSpawn);
    }

    public List<Enemy> createBackupEnemies() {
        return createEnemies(backupSpawn);
    }

    public boolean hasBackupSpawn() {
        return backupSpawn.length > 0;
    }

    public String getDisplayName() {
        String lower = name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    public String getEnemyPoolSummary() {
        String initial = describeSpawn(initialSpawn);
        if (backupSpawn.length == 0) {
            return "Initial: " + initial + " | No backup";
        }
        return "Initial: " + initial + " | Backup: " + describeSpawn(backupSpawn);
    }

    private static String describeSpawn(EnemyType[] spawn) {
        Map<EnemyType, Integer> counts = new EnumMap<>(EnemyType.class);
        for (EnemyType t : spawn) counts.merge(t, 1, Integer::sum);
        List<String> parts = new ArrayList<>();
        for (Map.Entry<EnemyType, Integer> e : counts.entrySet()) {
            parts.add(e.getValue() + " " + e.getKey().getDisplayName() + (e.getValue() > 1 ? "s" : ""));
        }
        return String.join(" + ", parts);
    }

    private List<Enemy> createEnemies(EnemyType[] enemyTypes) {
        Map<EnemyType, Integer> totals = new EnumMap<>(EnemyType.class);
        Map<EnemyType, Integer> seen = new EnumMap<>(EnemyType.class);
        List<Enemy> enemies = new ArrayList<>();

        for (EnemyType enemyType : enemyTypes) {
            totals.merge(enemyType, 1, Integer::sum);
        }

        for (EnemyType enemyType : enemyTypes) {
            int index = seen.merge(enemyType, 1, Integer::sum);
            String name = totals.get(enemyType) == 1
                ? enemyType.getDisplayName()
                : enemyType.getDisplayName() + " " + (char) ('A' + index - 1);
            enemies.add(enemyType.createInstance(name));
        }

        return enemies;
    }
}
