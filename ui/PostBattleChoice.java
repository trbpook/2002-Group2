package ui;

public enum PostBattleChoice {
    REPLAY_SAME_SETTINGS("Replay Same Settings"),
    NEW_GAME("Start New Game"),
    EXIT("Exit");

    private final String displayName;

    PostBattleChoice(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
