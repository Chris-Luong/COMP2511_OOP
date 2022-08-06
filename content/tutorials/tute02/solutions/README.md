# Tutorial 2

## A. Code Review (10 minutes)

Your tutor will provide you a link or open up the `src/shapes`, look at the `Shape` and `Rectangle` classes.

In groups, analyse the classes to answer the following questions:

1. What is the difference between `super` and `this`?
2. What about `super(...)` and `this(...)`?
4. What are static fields and methods?

> `super`: The instance of the super class
`this`: The instance of this class (like self in python)
`super(...)` Runs the respective method in the super class
`'this(...)'` Runs the respective method in this class with the given parameters (used for method overloading)

## B. JavaDoc (5 minutes)

Within the `src` directory, create a new package called `employee`.

Create an `Employee` class which has private fields for an employee's name and salary and appropriate getters, setters, and constructors. Document the class with JavaDoc.

## C. Basic Inheritance & Polymorphism (15 minutes)

* How many constructors should the class have? What arguments should they take?

    > Only one. If we don't define a constructor Java automatically generates one that takes no arguments. It makes little sense to have an employee with no name or salary, so the constructor should take the name and salary as arguments.

Create a `Manager` class that is a subclass of `Employee` and has a field for the manager's hire date.

> See code

* What constructors are appropriate?

    > Because we are inheriting from Employee, Java forces us to write a constructor that calls `super(...)`, so we have to write at least one. In this case, it makes sense to have a constructor that takes the name, salary and hire date, but you could also argue there should be one that just takes the name and salary and sets the hire date to the current day. It depends on context of how the class will be used whether you want the former, the latter, or both constructors.

* Is appropriate to have a getter for the hire date? What about a setter?

    > One can assume that if the hire date is stored it is information that will be needed at some point, so a getter is necessary. However, unlike the name or salary, it is unlikely that the hire date will be updated, so a setter would only serve to break that reasonable assumption about the class.

Override the `toString()` method inherited from `Object` in both classes.

> See code

* What should the result of `toString()` contain?

    > The [documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#toString--) for the `toString()` method states that it should return a string that "textually represents" the object. In this case, it should contain the name, salary and hire date (in the case of `Manager`), but also the runtime class of the object.

* Does the method in `Manager` call the one in `Employee`?

    > In order to satisfy the above requirement and not introduce unnecessary repetition, the superclass method must be called.

## D. Access Modifiers & Packages (10 minutes)

In the code in the `src/access` package, answer the questions marked `TODO`.

<img src='https://media.geeksforgeeks.org/wp-content/cdn-uploads/Access-Modifiers-in-Java.png' />