package action;

import java.util.List;
import model.Combatant;

public abstract class Action {
    protected Combatant actor;
    protected List<Combatant> targets;
    TargetMode targetStatus;

    protected static List<Combatant> wrapTarget(Combatant target) {
        return target != null ? List.of(target) : null;
    }

    public Action(Combatant actor, List<Combatant> targets) {
        this.actor = actor;
        this.targets = targets;
        if (this.targets == null || this.targets.isEmpty()) {
            this.targets = List.of();
            targetStatus = getDefaultTargetMode();
        } else {
            targetStatus = TargetMode.TARGET_RESOLVED;
        }
    }

    public Action(Combatant actor, Combatant target) {
        this(actor, wrapTarget(target));
    }

    public Action(Combatant actor) {
        this(actor, (Combatant) null);
    }

    public void setTargets(List<Combatant> targets) {
        this.targets = targets;
        targetStatus = TargetMode.TARGET_RESOLVED;
    }

    public void setSingleTarget(Combatant target) {
        this.targets = List.of(target);
        targetStatus = TargetMode.TARGET_RESOLVED;
    }

    public TargetMode getTargetStatus() {
        return targetStatus;
    }

    public void execute() {
        for (Combatant target : targets) {
            executeSingle(target);
        }
    }

    public abstract void executeSingle(Combatant target);

    public abstract TargetMode getDefaultTargetMode();
}
