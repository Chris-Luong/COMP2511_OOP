package dungeonmania.entities;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    public boolean hasBeenTriggered;
    public FloorSwitch(Position position, String type, String id) {
        super(position, type, id);
    }
    public boolean isTriggered() {
        return hasBeenTriggered;
    }
    public void setTrigger(boolean hasBeenTriggered) {
        this.hasBeenTriggered = hasBeenTriggered;
    }
}
