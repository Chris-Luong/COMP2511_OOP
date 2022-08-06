package dungeonmania.entities;

import dungeonmania.util.Position;

public class BuildableEntity extends Entity {
    private int durability;
    
    public BuildableEntity(Position position, String type, String id, int durability) {
        super(position, type, id);
        this.durability = durability;
    }
    public void tick() {
    }
    public int getDurability() {
        return durability;
    }
    public void setDurability(int durability) {
        this.durability = durability;
    }
    
}
