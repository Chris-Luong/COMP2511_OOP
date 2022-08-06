package dungeonmania.entities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Hydra extends MovableEntity {
    private boolean justSpawned;
    public Hydra(Position position, String type, String id, String gameMode) {
        super(position, type, id, gameMode);
        justSpawned = true;
    }

    public Position directionToPosition(Direction direction) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        
        switch(direction) {
            case UP:
                return new Position(x, y - 1, 1);
            case DOWN:
                return new Position(x, y + 1, 1);
            case LEFT:
                return new Position(x - 1, y, 1);
            case RIGHT :
                return new Position(x + 1, y, 1);
            case NONE :
                return new Position(x, y, 1);
            default:
                return getPosition();
        }
    }

    public boolean isJustSpawned() {
        return justSpawned;
    }

    public void setJustSpawned() {
        this.justSpawned = false;
    }

    public void tick(Direction direction) {
        Position newPosition = directionToPosition(direction);
        setPosition(newPosition);
    }
}
