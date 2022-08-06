package dungeonmania.entities;

import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity {
    private boolean holdingHealthPotion;
    private int hpFromPotion;

    public HealthPotion(Position position, String type, String id) {
        super(position, type, id);
        this.hpFromPotion = 50;
        setIsInteractable(true);
    }

    public boolean checkHasHealthPotion() {
        return holdingHealthPotion;
    }

    public void useHealthPotion() {
        holdingHealthPotion = false;
    }
}
