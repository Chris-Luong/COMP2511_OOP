package dungeonmania.entities;

import dungeonmania.util.Position;

public class Wood extends CollectableEntity {
    public Wood(Position position, String type, String id) {
        super(position, type, id);
    }

    // add method in level to store number of wood collected
    // also needs to account for if wood is consumed in the process of making a bow
}
