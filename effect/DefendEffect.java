package effect;

public class DefendEffect extends StatusEffect {
    private static final int DURATION = 2; // Lasts for 2 turns
    private static final int DEFENSE_BONUS = 10; // Example defense bonus
    private int defenseBonus;

    public DefendEffect() {
        super(DURATION);
        this.defenseBonus = DEFENSE_BONUS;
    }

    @Override
    public String getName() {
        return "Defend";
    }

    @Override
    public int getDefenseBonus() {
        return defenseBonus;
    }
    
}
