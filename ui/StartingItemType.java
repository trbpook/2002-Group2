package ui;

import item.Item;
import item.Potion;
import item.PowerStone;
import item.SmokeBomb;
import model.Player;

public enum StartingItemType {
    POTION {
        @Override
        public Item createItem(Player player) {
            return new Potion();
        }
    },
    SMOKE_BOMB {
        @Override
        public Item createItem(Player player) {
            return new SmokeBomb();
        }
    },
    POWER_STONE {
        @Override
        public Item createItem(Player player) {
            return new PowerStone(player.createSpecialSkill(false));
        }
    };

    public abstract Item createItem(Player player);

    public String getDisplayName(Player player) {
        return createItem(player).getName();
    }

    public String getDescription(Player player) {
        return createItem(player).getDescription();
    }
}
