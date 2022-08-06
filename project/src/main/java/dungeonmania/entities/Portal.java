package dungeonmania.entities;

import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    //public Portal(Position position, String type, String id, String colour) {
    public Portal(Position position, String type, String id) { 
        super(position, type, id);
        // added this
        setSkin("BLUE");
    }
    // needs a method to control how the portal teleports a player to another portal
}
