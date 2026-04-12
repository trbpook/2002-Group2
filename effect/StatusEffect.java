package effect;

public abstract class StatusEffect {
    protected int duration;
    protected int turnsRemaining;

    public StatusEffect(int duration) {
        this.duration = duration;
        this.turnsRemaining = duration;
    }

    public void tick() {
        turnsRemaining = Math.max(0, turnsRemaining - 1);
    }

    public boolean isExpired() {
        return turnsRemaining == 0;
    }

    public boolean isInvulnerable() {
        return false;
    }

    public boolean preventsAction() {
        return false;
    }

    public int getAttackBonus() {
        return 0;
    }

    public int getDefenseBonus() {
        return 0;
    }

    public abstract String getName();

}
