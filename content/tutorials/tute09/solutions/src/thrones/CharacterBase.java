package thrones;

import java.util.List;

/**
 * A character in the simple grid game example.
 *
 * @author Robert Clifton-Everest
 *
 */
public abstract class CharacterBase implements Character {
    private int healthPoints;

    private int x, y;

    public CharacterBase(int x, int y) {
        healthPoints = 100;
        this.x = x;
        this.y = y;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Cause this character the given amount of damage.
     *
     * @param points
     */
    public void damage(int points) {
        healthPoints -= points;
    }

    public String toString() {
        return getClass().getSimpleName() + " at (" + getX() + ", " + getY() + "), health = " + healthPoints;
    }

}
