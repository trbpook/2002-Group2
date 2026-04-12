package model;

import action.*;

public class Warrior extends Player {
    public Warrior() {
        super(260, 40, 20, 30);
    }

    @Override
    public SpecialSkill createSpecialSkill() {
        return null;
    }
}