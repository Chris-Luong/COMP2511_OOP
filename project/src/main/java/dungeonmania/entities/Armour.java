package dungeonmania.entities;

import dungeonmania.util.Position;

public class Armour extends CollectableEntity {
    private int armourDurability;
    public Armour(Position position, String type, String id, int armourDurability) {
        super(position, type, id);
        this.armourDurability = armourDurability;
    }
    public int getArmourDurability() {
        return armourDurability;
    }
    public void setArmourDurability(int armourDurability) {
        this.armourDurability = armourDurability;
    }
}
