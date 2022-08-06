package dungeonmania.entities;

import dungeonmania.util.Position;

public class Door extends StaticEntity {
    public boolean hasBeenOpened;
    public int doorCode;
    public Door(Position position, String type, String id) {
        super(position, type, id);
        this.doorCode = doorCode;
    }
    public boolean isOpen() {
        return hasBeenOpened;
    }
    // once a door is opened it remains open
    public void setOpened() {
        this.hasBeenOpened = true;
    }
    public void setDoorCode(int doorCode) {
        this.doorCode = doorCode;
    }
    public int getDoorCode() {
        return doorCode;
    }
}
