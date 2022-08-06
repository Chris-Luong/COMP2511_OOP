package dungeonmania.entities;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class MovableEntity extends Entity {

    private int health;
    private int attack;
    private String gameMode;

    public MovableEntity(Position position, String type, String id, String gameMode) {

        super(position, type, id);
        this.gameMode = gameMode;
        //this.health = health;
        //this.attack = attack;
        //this.move = move;
    }

    public String getGameMode() {
        return gameMode;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public boolean isDead() {

        if (health <= 0) {
            return true;
        }

        return false;
    }
    public void tick() {
        //move.tick();
    }
}