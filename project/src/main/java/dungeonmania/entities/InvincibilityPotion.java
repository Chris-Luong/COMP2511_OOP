package dungeonmania.entities;

import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity {
    private boolean holdingInvincibilityPotion;

    public InvincibilityPotion(Position position, String type, String id) {
        super(position, type, id);
        setIsInteractable(true);
    }

    public boolean checkHasInvincibilityPotion() {
        return holdingInvincibilityPotion;
    }

    public void useInvincibilityPotion() {
        holdingInvincibilityPotion = false;
    }

    // needs a method that makes the character invinicible
}
