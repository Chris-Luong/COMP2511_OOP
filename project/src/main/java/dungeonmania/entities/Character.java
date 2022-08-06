package dungeonmania.entities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Character extends MovableEntity {
    private boolean holdingKey;
    private int health;
    private int attack;

    public Character(Position position, String type, String id, int health, int attack, String gameMode) {

        super(position, type, id, gameMode);
        this.health = health;
        this.attack = attack;
        // change position settings to the starting point once that has 
        // been determined (preferably using getters)
    }

    public boolean isInvisible;
    public int invisibilityDuration;
    public boolean isInvincible;
    public int invincibilityDuration;

    private static final int CHARACTER_HEALTH = 200;
    private static final int CHARACTER_HEALTH_HARD = 150;
    
    public Character(Position position, String type, String id, String gameMode) {

        super(position, type, id, gameMode);
        setStats();
        
    }

    public void setStats() {

        setAttack(70);
        
        if (getGameMode().equals("hard")) {
            setHealth(CHARACTER_HEALTH_HARD);
        }
        else {
            setHealth(CHARACTER_HEALTH);
        }
    }

    @Override
    public int getHealth() {
        return super.getHealth();
    }

    @Override
    public void setAttack(int attack) {
        super.setAttack(attack);
    }

    public boolean currentlyInvincible() {
        return isInvincible;
    }
    public void setIsInvincible(boolean isInvincible) {

        if (!(getGameMode().equals("hard"))) {
            this.isInvincible = isInvincible;
            this.invincibilityDuration = 5;

        }
        
    }

    public boolean getIsInvincible() {
        return isInvincible;
    }

    public boolean currentlyInvisible() {
        return isInvisible;
    }
    public void setIsInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
        this.invincibilityDuration = 5;

    }

    // reads in from the command line and updates movement based on user input
    // TODO: Update with constraints for movement (e.g: can't move left if a wall is there)
    public void tick(String itemUsed, Direction direction) {
        updatePotionStats();
        //System.out.println("Current position :: X : " + getPosition().getX() + ", Y : " + getPosition().getY());
        Position newPosition = directionToPosition(direction, this.getPosition());
        setPosition(newPosition);
    }

    public void updatePotionStats() {
        this.invincibilityDuration -= 1;
        this.invisibilityDuration -= 1;

        if (invincibilityDuration <= 0) {
            this.isInvincible = false;
        }

        if (invisibilityDuration <= 0) {
            this.isInvisible = false;
        }
    }

    public void heal() {
        if (getGameMode().equals("hard")) {
            setHealth(CHARACTER_HEALTH_HARD);
        }
        else {
            setHealth(CHARACTER_HEALTH);
        }
    }

    public Position directionToPosition(Direction direction, Position position) {
        int x = position.getX();
        int y = position.getY();
        
        // System.out.println("The direction is " + direction);

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
                return position;
        }
    }

    public boolean hasKey() {
        return this.holdingKey;
    }
    public void useKey() {
        this.holdingKey = false;
    }
}
