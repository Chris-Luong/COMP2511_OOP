package dungeonmania.entities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class StaticEntity extends Entity {
    
    public StaticEntity(Position position, String type, String id) {
        super(position, type, id);
    }
    public void tick(String itemUsed, Direction direction) {

    }
}
