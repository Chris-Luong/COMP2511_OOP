package dungeonmania.entities;

import dungeonmania.util.Position;

public class Treasure extends CollectableEntity {

    public Treasure(Position position, String type, String id) {
        super(position, type, id);
        setIsInteractable(true);
    }
}
