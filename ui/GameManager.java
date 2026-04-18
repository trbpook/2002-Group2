package ui;

import engine.BattleEngine;
import model.Player;

public final class GameManager {
    private final ConsoleGameUI gameUI;

    public GameManager(ConsoleGameUI gameUI) {
        if (gameUI == null) {
            throw new IllegalArgumentException("gameUI cannot be null.");
        }
        this.gameUI = gameUI;
    }

    public void run() {
        gameUI.displayWelcomeScreen();
        GameConfiguration configuration = gameUI.promptNewGameConfiguration();

        while (true) {
            Player player = configuration.createPlayer();
            BattleEngine engine = new BattleEngine(player, configuration.getLevel(), gameUI);
            BattleEngine.BattleResult result = engine.startBattle();
            PostBattleChoice postBattleChoice = gameUI.promptPostBattleChoice(result);

            if (postBattleChoice == PostBattleChoice.REPLAY_SAME_SETTINGS) {
                continue;
            }

            if (postBattleChoice == PostBattleChoice.NEW_GAME) {
                configuration = gameUI.promptNewGameConfiguration();
                continue;
            }

            gameUI.displayGoodbye();
            return;
        }
    }
}
