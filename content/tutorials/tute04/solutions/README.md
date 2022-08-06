# Tutorial 04

## A. Design Principles (25 minutes)

### Part 1: Law of Demeter

In the `unsw.training` package there is some skeleton code for a training system. 

* Every employee must attend a whole day training seminar run by a qualified trainer
* Each trainer is running multiple seminars with no more than 10 attendees per seminar

In the TrainingSystem class there is a method to book a seminar for an employee given the dates on which they are available. This method violates the principle of least knowledge (Law of Demeter).

1. How and why does it violate this principle?

    > The `TrainingSystem` class extracts instances of `Seminar` from instances of `Trainer` and calls the methods of `Seminar`. Furthermore it extracts the start date from instances of `Seminar` and calls its methods. More informally, the `TrainingSystem` class is interacting with classes other than its "friends".

2. In violating this principle, what other properties of this design are not desirable?

    > * The design is needlessly tightly coupled as `TrainingSystem` is dependent on both `Trainer` and `Seminar`.
    > * `TrainingSystem` suffers from low cohesion as any change to the system requires a change to this class.
    > * The `Seminar` class has no control over the number attendees. It relies on `TrainingSystem` to ensure there are never more than 10. This makes `Seminar` hard to re-use as any future client has to ensure they don't exceed the maximum of 10 attendees. This is an example of poor encapsulation.

3. Refactor the code so that the principle is no longer violated. How has this affected other properties of the design?

    > * The design is no longer tightly coupled. `TrainingSystem` no longer has any knowledge of `Seminar`.
    > * Each of the classes now has a clear purpose in booking a training seminar, thus improving cohesion.
    > * The `Seminar` class itself is now responsible for ensuring that the number of attendees does not exceed 10. This is an example of a class maintaining its invariant. We'll come back to that when discussing programming by contract.

4. More generally, are getters essentially a means of violating the principle of least knowledge? Does this make using getters bad design?

    > Getters that return an object (as opposed to a primitive) likely only serve the purpose of letting clients invoke methods on that object, so a valid point can be made that getters can only be used as a means for violating the principle of least knowledge. A counter argument is that getters make classes more reusable. A client may need to do something with an object for which it has no method. In that case, getters can allow the client use the class in a way that was not originally foreseen, even if it does violate the principle of least knowledge.
    >
    > Another way in which a getter can be an example of bad design is in `Seminar` above. By having a `getAttendees()` method implemented as a simple getter, any client is able to add additional attendees to the list of attendees, potentially exceeding the maximum of 10. Unfortunately, Java does not offer any good solutions to this problem. Either `getAttendees()` has to create a copy of the list, or it can use `Collections.unmodifiableList(...)` to wrap the list up in a class that prevents any modification to the list. The former solution is inefficient as it needlessly copies data. The latter can be surprising to the client as the returned list still has an `add(...)` method, but it causes a runtime error every time it is used. Other languages resolve this problem by having proper immutable or read-only lists.

### Part 2: Liskov Substitution Principle

Look at the `OnlineSeminar` class. How does this violate the Liskov Substitution Principle?

> This class violates the Liskov Substitution Principle. Specifically a `Seminar` is defined as having a list of attendees, but `OnlineSeminar` does not require this. A client interacting with a `Seminar` would expect the seminar to be booked like any other. This is an example of classes having an IS-A relationship informally, but not a valid inheritance relationship when taking into account what the classes actually do and represent.


## B. Streams and Lambdas

1. Inside `src/stream/App.java`, rewrite the following code using the `.forEach()` method and a lambda:

```java
List<String> strings = new ArrayList<String>(Arrays.asList(new String[] {"1", "2", "3", "4", "5"}));
for (String string : strings) {
   System.out.println(string);
}
```

2. In the above example, discuss two different ways to write lambda expressions.

> One-liner or with curly braces

3. What is a stream? Rewrite the following code to use a stream and the `map` function.

```java
List<String> strings2 = new ArrayList<String>(Arrays.asList(new String[] {"1", "2", "3", "4", "5"}));
List<Integer> ints = new ArrayList<Integer>();
for (String string : strings2) {
    ints.add(Integer.parseInt(string));
}
```

4. Modify your answer to (3) to use a scope operator instead of a normal lambda.

> See (solutions/src/streams/App.java)[solutions/src/streams/App.java]

> [Lecture code examples](https://gitlab.cse.unsw.edu.au/COMP2511/21T3/content/-/tree/master/lectures/MyLambdaPipeline) & [slides](https://www.cse.unsw.edu.au/~cs2511/21T3/lectures/week04/pdfs/JavaLambdaAggregateOps.pdf).

## C. Design by Contract

1. What is Design by Contract? 

2. Discuss briefly as a class how you have used Design by Contract already in previous courses.

> 1531 API & Frontend/backend, 1511/2521 using ADTs

3. Discuss how Design By Contract was applied in the Blackout assignment.

> Key things to discuss here:
>   * We specified the interface functions in `Blackout`, and so long as they matched that interface, they could implement however they chose
>   * We told them that they didn't have to account for invalid Satellite/Device IDs, and that no 2 devices would ever occupy the same position - examples of preconditions which they didn't have to account for (if we did, the behaviour of the ADT is undefined)

4. In the `calculator` code, specify a contract for each of the `compute` functions. Hint: for the trig functions, look at the interface of the `Math` functions in the Java documentation. Key edge cases to consider:
    * Dividing by zero
    * `tan(Math.PI / 2)`

5. Will you need to write unit tests for something that doesn't meet the preconditions? Explain why.

6. We will now make our code more defensive by throwing an exception on the two edge cases listed above. Define your own custom exception called `CalculatorException`, and change the code so that these edge cases cause this exception to be thrown. Modify your contract accordingly. If you have time, add tests for these conditions as well.

> When you update your postconditions, include the possible exception cases
> Note that exception cases are still cases for which the preconditions aren't met - this is the subject of a bit of debate, because technically exceptional behaviour is 'accounted for' and defined which means the preconditions (user of the ADT) don't have to worry about it, but also looking at it from a formal 'proving-things about programs' perspective, an input which causes an exception to be raised doesn't map to an output, which means it's not formally 'defined' behaviour. So what this essentially means is that an input that doesn't meet the 'correct' preconditions will cause an exception to be raised (in a defensive programming style) but the behaviour of the ADT is still defined. 

> From a library-writing perspective this sort of 'contract except I tell you where you went wrong you if you mess up' style of design is useful for 2 reasons, one is that it prevents weird stuff from happening that breaks everything, and the second is that it helps users of the ADT debug their code. Just like how when you enter jibberish on a date field in a web form it says 'error: invalid date' etc etc instead of a 500 Internal Server Error, or when you divide by 0 in Python or Java you get a ZeroDivisionError, rather than some message from the OS.
