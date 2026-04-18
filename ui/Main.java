package ui;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ConsoleGameUI gameUI = new ConsoleGameUI();
        GameManager gameManager = new GameManager(gameUI);
        gameManager.run();
    }
}
