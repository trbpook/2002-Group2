package effect;

public abstract class StatusEffect {
    protected int duration;
    protected int turnsRemaining;

    public StatusEffect(int duration) {
        this.duration = duration;
    }

    public void tick() {
        turnsRemaining = Math.max(0, turnsRemaining - 1);
    }

    public boolean isExpired() {
        return turnsRemaining > 0;
    }

    public boolean isInvulnerable() {
        return false;
    }

    public boolean preventsAction() {
        return false;
    }

    public abstract String getName();

    public abstract int getAttackBonus();

    public abstract int getDefenseBonus();
}
