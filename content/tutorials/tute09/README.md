# Tutorial 09

## A. The Game 

In a simple game, different types of characters move around on a grid fighting each other. When one character moves into the square occupied by another they attack that character and inflict damage based on random chance (e.g. a dice roll).

* A king can move one square in any direction (including diagonally), and always causes 8 points of damage when attacking.
* A queen can move to any square in the same column, row or diagonal as she is currently on, and has a 1 in 3 chance of inflicting 12 points of damage or a 2 out of 3 chance of inflicting 6 points of damage.
* A dragon can only move up, down, left or right, and has a 1 in 6 chance of inflicting 20 points of damage.

Use a suitable Design Pattern to model a solution to this problem. The code has been started for you inside the `Character` class.

## B. Evolution of Requirements 

This exercise continues on from Exercise A.

Suppose a requirements change was introduce that necessitated support for different sorts of armour.

* A helmet reduces the amount of damage inflicted upon a character by 1 point.
* Chain mail reduces the amount of damage by a half (rounded down).
* A chest plate caps the amount of damage to 7, but also slows the character down. If the character is otherwise capable of moving more than one square at a time then this armour restricts each move to distances of 3 squares or less (by manhattan distance).

Use a suitable Design Pattern to realise these new requirements. Assume that, as this game takes place in a virtual world, there are no restrictions on the number of pieces of armour a character can wear and that the "order" in which armour is worn affects how it works. You may need to make a small change to the existing code.

## C. Building a Game

Use the Builder Pattern to abstract the creation of objects, allowing for a game to be created in the following manner:

```java
new GameBuilder(width, height)
    .createGame()
    .addQueen(x, y)
    .addKing(dx, dy)
    .addDragon();
```

The first entity is added at `(x, y)`. From the second entity onwards, these parameters become a `(dx, dy)` a relative position to that of the previous entity. Dragons have a random `x` and `y` chosen on construction.

How is this different from the Factory Pattern we used in Week 7? How does this design give us extra extensibility?