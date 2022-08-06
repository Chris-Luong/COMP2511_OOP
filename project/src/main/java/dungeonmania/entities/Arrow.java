package dungeonmania.entities;

import dungeonmania.util.Position;

public class Arrow extends CollectableEntity {
    public Arrow(Position position, String type, String id) {
        super(position, type, id);
    }

    // add method in level to store number of arrows collected
    // also needs to account for if wood is consumed in the process of making a bow
    // or in the process of shooting enemies
}
