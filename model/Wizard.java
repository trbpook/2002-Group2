package model;

import action.*;

public class Wizard extends Player {
    Wizard() {
        super(200, 50, 10, 20);
    }

    public SpecialSkill createSpecialSkill() {
        return null;
    }
}
