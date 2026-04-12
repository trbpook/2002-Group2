package model;

import action.SpecialSkill;
import action.ArcaneBlast;

public class Wizard extends Player {
    public Wizard() {
        super(200, 50, 10, 20);
    }

    public SpecialSkill createSpecialSkill(boolean resetCD) {
        return new ArcaneBlast(this, resetCD);
    }
}
