package dungeonmania.entities;

import dungeonmania.util.Position;

public class Sword extends CollectableEntity {
    private int swordDurability;
    public Sword(Position position, String type, String id, int swordDurability) {
        super(position, type, id);
        this.swordDurability = swordDurability;
        setIsInteractable(true);
    }
    public int getSwordDurability() {
        return swordDurability;
    }
    public void setSwordDurability(int swordDurability) {
        this.swordDurability = swordDurability;
    }
}
