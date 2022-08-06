# General
- Cannot change game mode/difficulty during a game.
- Battle mechanics: health, durability (e.g. character health is 200 and each attack is 10)
- Inventory: only 1 sword and armour can be in the inventory at any instance.
- Sword gives an extra 15 attack
- Invisibility potion lasts until a character does any action besides a move action
- Invincibility potion lasts for 10 ticks
- health potion heals the character for 100 hp

# Milestone 2

## Method Assumptions

### Gameplay & Level Assumptions
- A game contains 4 levels.
- Character's health resets after the end of each level.
- The inventory is carried on from level to level until the end of a game.

### Goal Assumptions
- There can only be a conjuction of two levels maximum.
- If there is no goal operator, that operator is classified as "NONE"

### Portals
- A level must contain a pair number of portals. 

### Building
- Priotise treasure removal instead of key removal when building a shield.

### Interact()
- Player can only interact with mercenaries and zombie spawners for Milestone 2.
Other types of entities will result in an IllegalArgumentException.
- Interactions only involve bribing mercernaries and attack zombie spawners. No
battles with mercernaries in this function.
- Being cardinally adjacent to a zombie spawner means we use the isAdjacent() method
rather than getAdjacentPositions() to loop through i.e. a zombie spawner can only
be destroyed if it is one tile above/below/to the left/to the right of the player.
- Being within 2 cardinal tiles of the mercernary means we implement something similar
to getAdjacentPositions() but with 2 tiles as the perimeter around the player i.e. not
just the compass positions of up, down, left and right but also diagonally.

### Entity Assumptions
- the exit is only opened once the character has completed all other goals
- a character can only hold 1 potion of each type at any given time, 
  e.g. one health potion and one invisibility potion can be held at the same time

### Combat
- The character can only combat movable entities.
- Zombies have a 1 in 5 chance of dropping armour when they die. 
- Mercenaries have a 1 in 3 chance of dropping armour when they die.

### Potions & Effects
- Invincibility and invisibility lasts 5 ticks/turns regardless of the game mode.

### Mercenary
- Battle radius is 3 tiles (could make an enum if there are lots of constants like this)
- Since Mercenary has the same moving mechanics as the player, they cannot move through walls
and can move boulders that are not blocked by a second boulder or a wall.
- The Mercenary is smart enough to follow the player, but not smart enough to perform evasive
manouvers, such as going around a wall after being cornered.

### Spider
- Spiders do not damage the player, they only count as enemies for use in the goals.
- Spiders instantly die when met by a player.
- Spiders have 10 damage and 2 hp (instantly dies in a battle with player).
- Maximum number of spiders on a map is 5.
- Spiders can move outside of the walls.