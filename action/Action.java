package action;

import java.util.List;
import model.Combatant;

public interface Action {

    String getLabel();

    TargetMode getTargetMode(Combatant actor);

    void execute(Combatant actor, List<Combatant> targets);
}