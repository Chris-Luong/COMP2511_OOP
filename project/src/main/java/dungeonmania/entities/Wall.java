package dungeonmania.entities;

import dungeonmania.util.Position;

public class Wall extends StaticEntity {

    public Wall(Position position, String type, String id) {
        super(position, type, id);
        setSkin("default");
    }
}
