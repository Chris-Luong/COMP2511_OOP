package dungeonmania.entities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovableEntity {

    private boolean justSpawned;
 
    
    public ZombieToast(Position position, String type, String id, String gameMode) {
        super(position, type, id, gameMode);
        setStats(gameMode);
        justSpawned = true;
    }

    public void setStats(String gameMode) {
        switch(gameMode) {
            case "peaceful":
                setAttack(0);
                setHealth(1);
                return;
            case "standard":
                setAttack(30);
                setHealth(30);
                return;
            case "hard":
                setAttack(40);
                setHealth(40);
                return;
        } 
    }
    
    public boolean isJustSpawned() {
        return justSpawned;
    }

    public void setJustSpawned() {
        this.justSpawned = false;
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

    public void tick(Direction direction) {
        Position newPosition = directionToPosition(direction);
        setPosition(newPosition);
    }

}
