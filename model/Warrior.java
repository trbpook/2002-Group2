package model;

import action.SpecialSkill;
import action.ShieldBash;

public class Warrior extends Player {
    public Warrior() {
        super(260, 40, 20, 30);
    }

    @Override
    public SpecialSkill createSpecialSkill(boolean resetCD) {
        return new ShieldBash(this, resetCD);
    }
}