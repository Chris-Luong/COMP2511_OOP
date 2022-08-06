package dungeonmania.entities;

import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity {
    private boolean holdingInvisibilityPotion;

    public InvisibilityPotion(Position position, String type, String id) {
        super(position, type, id);
        setIsInteractable(true);
    }

    public boolean checkHasHealthPotion() {
        return holdingInvisibilityPotion;
    }

    public void useHealthPotion() {
        holdingInvisibilityPotion = false;
    }
    // assumption invisibility potion only lasts a short while
    // needs function to make player undetectable by other entities
}
