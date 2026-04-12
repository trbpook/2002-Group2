package effect;

public class StunEffect extends StatusEffect {
    private static final int DURATION = 2; // Lasts for 2 turn

    public StunEffect() {
        super(DURATION);
    }

    @Override
    public String getName() {
        return "Stun";
    }

    @Override
    public boolean preventsAction() {
        return true;
    }
}
