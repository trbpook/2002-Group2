package model;

import action.SpecialSkill;
import action.ArcaneBlast;

public class Wizard extends Player {
    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
    }

    @Override
    public SpecialSkill createSpecialSkill(boolean resetCD) {
        return new ArcaneBlast(resetCD);
    }
}
