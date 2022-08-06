package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovableEntity {
    boolean justSpawned = false;
    boolean reversedDirection = false;
    
    Position startPos;
    // add in the boolean params later
    public Spider(Position position, String type, String id, String gameMode) {
        super(position, type, id, gameMode);
        startPos = position;
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



    public void setJustSpawned(boolean justSpawned) {
        this.justSpawned = justSpawned;
    }



    public boolean isReversedDirection() {
        return reversedDirection;
    }



    public void setReversedDirection(boolean reversedDirection) {
        this.reversedDirection = reversedDirection;
    }



    public Position getStartPos() {
        return startPos;
    }



    public void setStartPos(Position startPos) {
        this.startPos = startPos;
    }


    public Position directionToPosition(Direction direction) {
        int x = getPosition().getX();
        int y = getPosition().getY();

        switch(direction) { 
            case UP:
                return new Position(x, y - 1, 100);
            case DOWN:
                return new Position(x, y + 1, 100);
            case LEFT:
                return new Position(x - 1, y, 100);
            case RIGHT :
                return new Position(x + 1, y, 100);
            case NONE :
                return new Position(x, y, 100);
            default:
                System.out.println("Invalid Direction\n");
                return getPosition();
        }
    }

    public void tick() {
        // make a move method for normal direction (anticlockwise) and reversed direction,
        // takes parameter for if it is just spawned, in which case it use moves up once.
        // checkBoulder(currPos, );
        Position possiblePos = checkNextPosition();
        if (getPosition().getX() == startPos.getX() && getPosition().getY() == startPos.getY()) {
            System.out.println("justSpawned for spider is " + justSpawned);
            setPosition(possiblePos);
            justSpawned = false;
            return;
        }
        System.out.println("justSpawned for spider is " + justSpawned);
        setPosition(possiblePos);
    }

    public boolean checkBoulder(Position position) {
   
        return false;
    }

    public Position checkNextPosition() {

        Position possiblePos = getPosition();

        int startX = startPos.getX();
        int startY = startPos.getY();

        int currX = getPosition().getX();
        int currY = getPosition().getY();

        if (!isReversedDirection()) {
            possiblePos = getNextNormalPosition(possiblePos, startX, startY, currX, currY);
        } else {
            possiblePos = getNextReversedPosition(possiblePos, startX, startY, currX, currY);
        }

        System.out.println("Spider position check: (" + getPosition().getX() + "," + getPosition().getY() + "). isReversed is "+reversedDirection);
        return possiblePos;
    }

    public Position getNextNormalPosition(Position possiblePos, int startX, int startY, int currX, int currY) {
        // if in starting position and no boulder above, move up
        if (justSpawned) {
            possiblePos = directionToPosition(Direction.UP);
        }     
        // if above starting position, move right for the left 2 tiles
        else if ((currY == startY - 1) &&
            (currX == startX - 1 ||
            currX == startX)) {
            possiblePos = directionToPosition(Direction.RIGHT);
        }
        // if to the right of starting position, move down for the top 2 tiles
        else if ((currX == startX + 1) &&
            (currY == startY - 1 ||
            currY == startY)) {
            possiblePos = directionToPosition(Direction.DOWN);
        }
        // if below starting position, move left for the right 2 tiles
        else if ((currY == startY + 1) &&
            (currX == startX ||
            currX == startX + 1)) {
            possiblePos = directionToPosition(Direction.LEFT);
        }
        // if to the left of starting position, move up for the bottom 2 tiles
        else if ((currX == startX - 1) &&
            (currY == startY ||
            currY == startY + 1)) {
            possiblePos = directionToPosition(Direction.UP);
        }
        System.out.println("possiblePos of spider: ("+possiblePos.getX()+","+possiblePos.getY()+").");
        return possiblePos;        
    }

    public Position getNextReversedPosition(Position possiblePos, int startX, int startY, int currX, int currY) {
        // if in starting position and no boulder above, move down
        if (justSpawned) {
            possiblePos = directionToPosition(Direction.DOWN);
            justSpawned = false;
        }     
        // if above starting position, move left for the left 2 tiles
        else if ((currY == startY - 1) &&
            (currX == startX - 1 ||
            currX == startX)) {
            possiblePos = directionToPosition(Direction.LEFT);
        }
        // if to the right of starting position, move up for the top 2 tiles
        else if ((currX == startX + 1) &&
            (currY == startY - 1 ||
            currY == startY)) {
            possiblePos = directionToPosition(Direction.UP);
        }
        // if below starting position, move right for the right 2 tiles
        else if ((currY == startY + 1) &&
            (currX == startX ||
            currX == startX + 1)) {
            possiblePos = directionToPosition(Direction.RIGHT);
        }
        // if to the left of starting position, move down for the bottom 2 tiles
        else if ((currX == startX - 1) &&
            (currY == startY ||
            currY == startY + 1)) {
            possiblePos = directionToPosition(Direction.DOWN);
        }
        return possiblePos;        
    }
    /**
     * need to know the starting point of spider and current position to know where to move next
     * if statements with a few parameters with move() method within and enter direction depending on current position
     * 
     * have a reverse direction method that gets a direction and flips it in switch statement, returning new direction
     * 
     * 
     * if there is a boulder above and below the spider at spawn point, it does not move until boulders have been moved.
     * if it spawns on a boulder, it moves up and just moves to the right like normal, even if the boulder is pushed up
     * on to it in the next tick.  This is ASSUMPTION---------------------------------------------------------------------
     * 
     * When player is invincible, spider stops moving This is ASSUMPTION--------------------------------------------------
     * 
     * Either make it such that spider always spawns inside map or let it move out of map as assumption
     * 
     * Max spiders is 4
     */
}
