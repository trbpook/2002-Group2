package ui;

import engine.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import item.Item;
import model.Player;

public final class GameConfiguration {
    private final PlayerClassOption playerClass;
    private final List<StartingItemType> startingItems;
    private final Level level;

    public GameConfiguration(PlayerClassOption playerClass, List<StartingItemType> startingItems, Level level) {
        if (playerClass == null) {
            throw new IllegalArgumentException("playerClass cannot be null.");
        }
        if (startingItems == null || startingItems.size() != 2) {
            throw new IllegalArgumentException("startingItems must contain exactly two items.");
        }
        if (level == null) {
            throw new IllegalArgumentException("level cannot be null.");
        }

        this.playerClass = playerClass;
        this.startingItems = List.copyOf(startingItems);
        this.level = level;
    }

    public PlayerClassOption getPlayerClass() {
        return playerClass;
    }

    public List<StartingItemType> getStartingItems() {
        return Collections.unmodifiableList(startingItems);
    }

    public Level getLevel() {
        return level;
    }

    public Player createPlayer() {
        Player player = playerClass.createPlayer();
        for (StartingItemType itemType : startingItems) {
            Item item = itemType.createItem(player);
            player.getInventory().addItem(item);
        }
        return player;
    }

    public List<String> getStartingItemNames() {
        Player previewPlayer = playerClass.createPlayer();
        List<String> itemNames = new ArrayList<>();

        for (StartingItemType itemType : startingItems) {
            itemNames.add(itemType.getDisplayName(previewPlayer));
        }

        return itemNames;
    }
}
