package model;

public class Cooldown {
    private int duration;
    private int remaining;

    public Cooldown(int duration) {
        this.duration = duration;
        this.remaining = 0;
    }

    public void reset() {
        this.remaining = duration;
    }

    public void tick() {
        if (remaining > 0) {
            remaining--;
        }
    }

    public boolean isReady() {
        return remaining == 0;
    }

    public int getRemaining() {
        return remaining;
    }
}
