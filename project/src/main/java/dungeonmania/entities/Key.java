package dungeonmania.entities;

import dungeonmania.util.Position;

public class Key extends CollectableEntity {
    public int keyCode;
    public Key(Position position, String type, String id) {
        super(position, type, id);
        this.keyCode = keyCode;
    }
    public int getKeyCode() {
        return keyCode;
    }
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
