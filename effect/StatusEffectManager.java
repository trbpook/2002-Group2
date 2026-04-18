package effect;

import java.util.ArrayList;
import java.util.List;

public class StatusEffectManager {
    private final List<StatusEffect> activeEffects;

    public StatusEffectManager() {
        activeEffects = new ArrayList<>();
    }

    public StatusEffectManager(List<StatusEffect> initialEffects) {
        activeEffects = new ArrayList<>(initialEffects);
    }

    public void addEffect(StatusEffect effect) {
        activeEffects.add(effect);
    }

    public boolean isInvulnerable() {
        return activeEffects.stream().anyMatch(StatusEffect::isInvulnerable);
    }

    public boolean preventsAction() {
        return activeEffects.stream().anyMatch(StatusEffect::preventsAction);
    }

    public int getTotalAttackBonus() {
        return activeEffects.stream().mapToInt(StatusEffect::getAttackBonus).sum();
    }

    public int getTotalDefenseBonus() {
        return activeEffects.stream().mapToInt(StatusEffect::getDefenseBonus).sum();
    }

    public List<String> getActiveEffectNames() {
        return activeEffects.stream().map(StatusEffect::getName).toList();
    }

    public void tick() {
        tickAll();
    }

    public void tickAll() {
        activeEffects.forEach(StatusEffect::tick);
        cleanExpired();
    }

    void cleanExpired() {
        activeEffects.removeIf(StatusEffect::isExpired);
    }

    void clearAll() {
        activeEffects.clear();
    }
}
