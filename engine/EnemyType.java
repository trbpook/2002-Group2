package engine;

import model.Enemy;
import model.Goblin;
import model.Wolf;

public enum EnemyType {
    GOBLIN("Goblin") {
        @Override
        public Enemy createInstance(String name) {
            return new Goblin(name);
        }
    },
    WOLF("Wolf") {
        @Override
        public Enemy createInstance(String name) {
            return new Wolf(name);
        }
    };

    private final String displayName;

    EnemyType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract Enemy createInstance(String name);
}
