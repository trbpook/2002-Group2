package effect;

public class ArcaneBlastBuff extends StatusEffect {
    private static final int DURATION = -1; // Infinite duration
    private int attackBonus;

    ArcaneBlastBuff(int attackBonus) {
        super(DURATION); // -1 indicates infinite duration
        this.attackBonus = attackBonus;
    }

    @Override
    public String getName() {
        return "Arcane Blast";
    }

    @Override
    public int getAttackBonus() {
        return attackBonus;
    }
}
