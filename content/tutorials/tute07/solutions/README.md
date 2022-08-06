# Tutorial 07 - Solutions

## A. Finding Patterns 

In groups, determine a possible pattern that could be used to solve each of the following problems:

* Sorting collections of records in different orders.

    > Strategy pattern. This what Java does with the `Collections.sort()` method. A `Comparator` can be provided to determine the order in which elements are sorted.

* Modelling a file system

    > Composite pattern. Both folders and files are filesystem entries. Files form the leaves, folders can contain files or other folders.

* Updating a UI component when the state of a program changes.

    > Observer pattern. If the state of the program is the subject and the UI an observer, the UI will be notified of any changes to the state and update accordingly.

* Parsing and evaluating arithmetic expressions.

    > Composite pattern. The composite pattern can be used to represent a parse-tree. An example of this is given in the code.

* Adjusting the brightness of a screen based on a light sensor.

    > Observer pattern. If the light sensor is the subject, the observer could be notified on all significant changes to the amount of light hitting the sensor and adjust the brightness of the screen accordingly.

## B. Composite Pattern 

> See solutions

Inside `src/calculator`, use the Composite Pattern to write a simple calculator that evaluates an expression. Your calculator should be able to:

* Add two expressions
* Subtract two expressions
* Multiply two expressions
* Divide two expressions

There should be a `Calculator` class as well which can be passed in an expression, and calculate that expression.

Design a solution, create stubs, write failing unit tests, then implement the functions.

## C. Factory Pattern

Inside `src/thrones`, there is some code to model a simple chess-like game. In this game different types of characters move around on a grid fighting each other. When one character moves into the square occupied by another they attack that character and inflict damage based on random chance. There are four types of characters:

* A king can move one square in any direction (including diagonally), and always causes 8 points of damage when attacking.
* A knight can move like a knight in chess (in an L shape), and has a 1 in 2 chance of inflicting 10 points of damage when attacking.
* A queen can move to any square in the same column, row or diagonal as she is currently on, and has a 1 in 3 chance of inflicting 12 points of damage or a 2 out of 3 chance of inflicting 6 points of damage.
* A troll can only move up, down, left or right, and has a 1 in 6 chance of inflicting 20 points of damage.

We won't concern ourselves with the logic of the game in this exercise per se, but instead the creation of objects.

We want to refactor the code so that when the characters are created, they are put in a random location in a grid of length 5.

1. How does the Factory Pattern (AKA Factory Method) allow us to abstract construction of objects, and how will it improve our design with this new requirement?

> We can abstract the construction of the character objects - this means we are not dealing directly with a constructor. In this case, we can abstract the generating of random numbers to someone else (the factory in this case) and simply call `CharacterFactory.createKing` etc, which makes our code more clean and less likely for `Game` to cause problems with creating the objects itself. 

2. Use the Factory Pattern to create a series of object factories for each of the character types, and change the `main` method of `Game.java` to use these factories.
