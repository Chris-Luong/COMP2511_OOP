package dungeonmania.entities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {

    private boolean isDestroyed;

    public ZombieToastSpawner(Position position, String type, String id) {
        super(position, type, id);
        this.isDestroyed = false;
        setIsInteractable(true);
    }

    public void tick() {

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

    // needs method to destory the spawner if character has a weapon and is adjacent
    public boolean checkSpawnerIsDestroyed() {
        return isDestroyed;
    }
    public void setDestroyed() {
        this.isDestroyed = true;
    }
}
