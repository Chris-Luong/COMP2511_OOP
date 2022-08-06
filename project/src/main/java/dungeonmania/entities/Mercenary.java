package dungeonmania.entities;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.util.Direction;

public class Mercenary extends MovableEntity {

    private String status;
    private Direction direction;
    List<Position> possiblePositions= new ArrayList<Position>();

    public Mercenary(Position position, String type, String id, String gameMode) {
        
        super(position, type, id, gameMode); //new moveMercenary());
        this.status = "hostile";
        setStats(gameMode);
        setIsInteractable(true);
    }

    public void setStats(String gameMode) {
        
        switch(gameMode) {
            case "peaceful":
                setAttack(0);
                setHealth(1);
                return;
            case "standard":
                setAttack(50);
                setHealth(5);
                return;
            case "hard":
                setAttack(70);
                setHealth(20);
                return;
        } 
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public void tick(Position playerPosition) {


    // public void tick(String itemUsed, Direction direction, Position playerPosition) {
        
        switch(status) {
            case "hostile" :
                // System.out.println(status);
                break;
            case "friendly" :
                // System.out.println(status);
                break;
            default :
                System.out.println("Merc status invalid\n");
        }
        if (possiblePositions.size() > 0) {
            setPosition(possiblePositions.get(0));
        }
        return;
    }

    public Position directionToPosition(Direction direction, Position mercPosition) {
        int x = mercPosition.getX();
        int y = mercPosition.getY();

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
                System.out.println("Invalid Direction\n");
                return getPosition();
        }
    }

    public List<Position> getPossiblePositions() {
        return this.possiblePositions;
    }

    /**
     * Finds the possible positions that a mercenary can move, disregarding walls and boulders.
     * @param playerPos
     * @return List<Position>
     */
    public List<Position> determinePossiblePositions(Position playerPos) {
        int playerX = playerPos.getX();
        int playerY = playerPos.getY();

        Position mercPos = this.getPosition();
        int mercX = mercPos.getX();
        int mercY = mercPos.getY();
        // Reset the possible positions every tick
        possiblePositions.clear();

        System.out.println("merc pos: ("+mercX+","+mercY+"). Player pos: ("+playerX+","+playerY+").");

        if (playerX < mercX) {
            setDirection(Direction.LEFT);
            possiblePositions.add(directionToPosition(Direction.LEFT, mercPos));
        }
        if (playerX > mercX) {
            setDirection(Direction.RIGHT);
            possiblePositions.add(directionToPosition(Direction.RIGHT, mercPos));
        }
        if (playerY < mercY) {
            setDirection(Direction.UP);
            possiblePositions.add(directionToPosition(Direction.UP, mercPos));
        }
        if (playerY > mercY) {
            setDirection(Direction.DOWN);
            possiblePositions.add(directionToPosition(Direction.DOWN, mercPos));
        }
        return possiblePositions;
    }

}
