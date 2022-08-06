package dungeonmania.entities;

import dungeonmania.DungeonManiaController;
import dungeonmania.goals.Goal;
import dungeonmania.goals.GoalPool;
import dungeonmania.util.Position;
import dungeonmania.Level;

public class Exit extends StaticEntity {
    private boolean opened;
    public Exit(Position position, String type, String id) {
        super(position, type, id);
    }

    public boolean getOpened() {
        return opened;
    }

    public void setOpenExit() {
        this.opened = true;
    }

    // have a function which checks if all other goals have been completed
    // if this is true then we call setOpenExit()
    // check if character has gone through the exit - (check positions and make sure exit is open)
    // if this is true then we end the level

    public void tick(Level level) {
        if (level.checkToOpenExit(this)) {
            this.setOpenExit();
        }

    }
}