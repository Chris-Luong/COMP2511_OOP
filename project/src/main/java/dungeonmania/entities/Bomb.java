package dungeonmania.entities;

import dungeonmania.util.Position;

public class Bomb extends CollectableEntity {
    public Bomb(Position position, String type, String id) {
        super(position, type, id);
        setIsInteractable(true);
    }

    // need function in level to check if the bomb is placed near a switch
    // also needs function to destory all entities near the bomb blast except the character
}
