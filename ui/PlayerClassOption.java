package ui;

import model.Player;
import model.Warrior;
import model.Wizard;

public enum PlayerClassOption {
    WARRIOR(
        "Warrior",
        "260 HP, 40 ATK, 20 DEF, 30 SPD",
        "Special Skill: Shield Bash deals attack damage and stuns one enemy."
    ) {
        @Override
        public Player createPlayer() {
            return new Warrior();
        }
    },
    WIZARD(
        "Wizard",
        "200 HP, 50 ATK, 10 DEF, 20 SPD",
        "Special Skill: Arcane Blast hits all enemies and grants +10 ATK per kill."
    ) {
        @Override
        public Player createPlayer() {
            return new Wizard();
        }
    };

    private final String displayName;
    private final String statsSummary;
    private final String specialSkillSummary;

    PlayerClassOption(String displayName, String statsSummary, String specialSkillSummary) {
        this.displayName = displayName;
        this.statsSummary = statsSummary;
        this.specialSkillSummary = specialSkillSummary;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getStatsSummary() {
        return statsSummary;
    }

    public String getSpecialSkillSummary() {
        return specialSkillSummary;
    }

    public abstract Player createPlayer();
}
