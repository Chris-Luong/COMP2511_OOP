package dungeonmania.entities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;

public class Entity {

    private String id;
    private Position position;
    private String skin;
    private String type;
    
    private boolean isInteractable;
    // TODO : isInteractable

    public Entity(Position position, String type, String id) {
        // entity Ids will be assigned as "d-" + the entity's order
        // i.e. 1st entity is "e-1", 2nd entity is "e-2", etc.
        this.id = id;
        this.position = position;
        this.type = type;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }

    public String getType() {
        return type;
    }
    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public boolean getIsInteractable() {
        return isInteractable;
    }

    // TODO: check if it can interact or smthn
    public void setIsInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public void tick(String itemUsed, Direction direction) {

    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, type, position, true);
    }

    public static boolean isAdjacent(Position a, Position b) {
        return Position.isAdjacent(a, b);
    }
}
