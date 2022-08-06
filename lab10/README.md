# Lab 10

### Due: Week 10 Sunday, 5pm

### Value: 2 marks towards the Class Mark

This lab will be partially automarked, and partially manually marked.

## Aims

* Apply the Iterator Pattern to non-linear data structures
* Revise Graphs, Depth-First Search and Breadth-First Search
* Understand the concepts of Iterators and Iterables
* Apply the Visitor Pattern
* Apply the Adapter Pattern

## Setup

**REMEMBER** to replace the zID below with your own.

`git clone gitlab@gitlab.cse.EDU.AU:COMP2511/21T3/students/z555555/lab10.git`

## Lab 10 - Exercise - Graph Iterator 🔗

The Iterator Pattern allows us to traverse a data structure whilst maintaining the abstraction of the data structure's underlying details. An iterator is a black-box that we can keep asking to give us elements, until it runs out. This occurs in a *linear* fashion (we ask for elements one at a time), and the Iterator Pattern hence allows us to *linearise* any data structure. 

For `Set`s, `ArrayList`s and plenty of other data structures this is all very well because the ADT itself is already linear, conceptually. But what about something that's non-linear, like a graph? So long as we have some sort of sequence to accessing elements of the data structure, we can build an iterator.

In this exercise, you will be using the Iterator Pattern to write two iterators - one which traverses a graph using **Breadth-First Search** and one that traverses a graph using **Depth-First Search**.

Inside `src/graph/Graph.java`, we have written a Generic `Graph` class which models an undirected graph using an **adjacency list** with a `HashMap`. To recall, an adjacency list stores the graph in the following format:

```
Node : [ All the nodes the node is adjacent to ]
```

### BFS

Create a new class called `BreadthFirstGraphIterator.java` that uses BFS to traverse a graph, given that graph and a starting node. Each subsequent call to the `next` method should 'tick' the BFS by one (i.e. the next element is looked at). You may not pre-traverse the graph and store the nodes to visit in an `ArrayList` or similar and simply pawn off the job to that.

BFS Pseudocode:

```
queue = []
visited = set()
while queue:
    vertex = queue.dequeue()
    visited.add(vertex)
    queue.extend(graph.get_adjacent(vertex) - visited)
```

Inside `Graph.java`, write a method called `breadthFirstIterator` which returns a new BFS iterator.

### DFS

Create a new class called `DepthFirstGraphIterator.java` that uses DFS to traverse a graph, given that graph and a starting node. Each subsequent call to the `next` method should 'tick' the DFS by one (i.e. the next element is looked at). You may not pre-traverse the graph and store the nodes to visit in an `ArrayList` or similar and simply pawn off the job to that.

Inside `Graph.java`, write a method called `depthFirstIterator` which returns a new DFS iterator.

If you need to brush up on Graphs, here are a few links to COMP2521 lectures:
* [Graph ADT](https://www.youtube.com/watch?v=4s_3uirIGM8&list=PLi2pCZz5m6GEftzPIxVH1ylwytux9WOGN&index=16)
* [Graph Implementations](https://www.youtube.com/watch?v=2hbR-aez1E4&list=PLi2pCZz5m6GEftzPIxVH1ylwytux9WOGN&index=17)
* [Graph Traversal](https://www.youtube.com/watch?v=DzdztZboQ6w&list=PLi2pCZz5m6GEftzPIxVH1ylwytux9WOGN&index=18)

Some simple tests have been provided for you inside `GraphTest.java`, they don't currently compile as the Iterator classes themselves do not exist.

The second test uses this Graph:

<img src='imgs/graph.png' width='500' />


<details>
<summary>Hints</summary>

* You will not be able to use recursion to do the DFS.
* Java provides collections which will help you with the implementation of the algorithm.

</details>

### Iterators & Iterables

Change the definition of `Graph` so that it is `Iterable`. By default, the graph will traverse itself using a BFS, starting with the first node that was added to the graph. Write a test for this that loops through a graph. 

Inside `iterator.md`, answer the following questions:

* Do you think making the `Graph` `Iterable` makes semantic sense? Discuss briefly, and think of both sides.
* We could change the definition of our `Graph` so that the traversal logic is done internally, i.e:

    ```java
    public class Graph<N extends Comparable<N>> implements Iterable<N>, Iterator<N>
    ```

* Is a `Graph` an iterator or an iterable in this case? 
* What would the `.iterator` method return in this case?
* There is a problem with this approach though. Inside `iterator.md`, describe a test that would cause this implementation to fail.


## Lab 10 - Exercise - A Visiting Calculator ⏳

In Tutorial 07, we created a simple calculator using the Composite Pattern. That was all very well, but if you recall we had to construct the entire expression ourselves using objects, such as the following:

```java
// (1 + ((2 * 3) / 4)) - 5
Expression expr = new SubtractExpression(
                    new AddExpression(
                        new ModulusExpression(
                            new MultiplyExpression(new ValueExpression(2), new ValueExpression(3)), 
                            new ValueExpression(4)), 
                        new ValueExpression(1)), 
                    new ValueExpression(5));
```

This isn't currently very helpful if we wanted to just take in the expression `1 + 2 * 3 / 4 - 5` and manipulate, print or compute it, which is what we would need to do if we were going to make a real calculator application. This problem is known as **parsing**, which is [an interesting problem](https://en.wikipedia.org/wiki/Parsing) but outside the scope of the course.

Inside `unsw/calculator/model/tree` is some legacy code that uses the Composite Pattern to implement a tree, and will parse a string such as `"1 + 2 * 3 / 4 - 5"`  to construct the expression tree. In this exercise, we want to write code which prints the tree in various notations and evaluates the tree.

If we were building the model from scratch, we would probably just use the Composite Pattern. However, in many legacy codebases you may come across the code will be too complex and/or brittle to simply rewrite or add to easily, and you will instead have to build new functionality around existing code. To do this, we will use the Visitor Pattern.

Use the Visitor Pattern to enable three operations on the Expression Tree:

* Print the Tree using [**infix notation**](https://en.wikipedia.org/wiki/Infix_notation). This is the commonly used notation for articulating arithmetic expressions. Given the above example, the expression in infix notation is `1 + 2 * 3 / 4 - 5`.
* Print the Tree using **postfix notation**, also known as [Reverse Polish Notation](https://en.wikipedia.org/wiki/Reverse_Polish_notation). Postfix Notation prints each of the operands and *then* the operator. For example `3 + 4` would be expressed as `3 4 +`. The above example would be expressed as `1 2 3 * 4 / + 5 -`.
* Evaluates the expression.

Inside `Visitor.java` we have provided you with an interface with two methods: `visitBinaryOperatorNode` and `visitNumericNode`. You will need to:
* Implement these methods in the classes `InFixPrintVisitor`, `PostFixPrintVisitor` and `EvaluatorVisitor` respectively;
* Modify `TreeNode` as needed to ensure that any class that extends/implements it must implement an `accept` method;
* Add an `acceptLeft` and an `acceptRight` method to `BinaryOperatorNode` to allow for the composite accepting of visitors.
* The starter code uses the Composite Pattern for the Infix printing operation to help you out, so you should remove that as well.
* In addition to the above, you also need modify the parsing code to support the modulus operator (`%`) in expressions.

We have provided you with some very basic tests inside `test/calculatorVisitorTest`. They are currently commented out as they don't compile. These tests must pass **without being changed** as our automarking will rely on the same class and method prototypes. You should write some additional tests to ensure your solution passes our autotests.

**We will be testing the contents of `stdout` to check your Infix and Postfix visitors (see the provided tests for an example), so please make sure you remove all your debugging print statements before submitting.**

<details>
<summary>Hint</summary>

In `EvaluatorVisitor`, you may wish to make use of a data structure to help you evaluate the expression.

</details>


## Lab 10 - Exercise - Calculator Adapter 🔌

This exercise follows on from the previous exercise. 

Inside `calculator/view`, there is some frontend code which renders an interface for a simple calculator. Now that we have completed the backend (model), we need to put the two together to create a working app. However, we have the problem that the interfaces between the view and the model are not compatible.

At the moment, there is a method inside the `CalculatorInterface` class `getEvaluator()`, which returns a `DummyEvaluator` object. This method is the point of contact between the backend and frontend - where the frontend can pass the backend an expression to compute and receive the result. Have a look at where this method is called and see how the frontend code works.

The `DummyEvaluator` which `implements Evaluator` has a method which returns `0`, which explains why when you run the application, enter an expression and press `=`, it spits out `0`. 

Your task is to use the Adapter Pattern to connect the backend and frontend. Complete `EvaluatorAdapter` which is of type `Evaluator` and computes a given expression using your code from the previous exercise.

We have provided some tests for you inside `test/calculatorAdapterTest`. These tests must pass **without being changed** as our automarking will rely on the same class and method prototypes. You will also be able to tell that the code is working via a Usability Test (run the `main` method in `CalculatorInterface` and test that the application works as it should).

Note that you will need to write very little code to solve this problem.

## Submission

To submit, make a tag to show that your code at the current commit is ready for your submission using the command:

```bash
$ git tag -fa submission -m "Submission for Lab-10"
$ git push -f origin submission
```

Or, you can create one via the GitLab website by going to **Repository > Tags > New Tag**.

We will take the last commit on your `master` branch before the deadline for your submission.

## Lab 10 - Bonus Exercise - Gratitude ❤️

This exercise isn't marked; it's just here to give you a chance to reflect and wrap up the course.

Inside `gratitude.md` (or maybe somewhere else you're likely to stumble across one day, like a Word Doc or a diary), briefly (or extensively if you want) jot down answers to the following questions:

* What have I learned in this course about good software design?
* What was the biggest challenge of this course for me?
* What is one thing I want to do better? (can be anything)
* What am I excited to learn next?

Once you've done this, go and thank three people who helped you throughout the course. It might be your tutor, your project teammates, or your friends and family who supported you.

<details>
<summary>Click here once you've finished the exercise ;)</summary>
<br/>
Well done :) You made it to the end of COMP2511, and to the end of the 5 Core Computer Science courses at UNSW.

### Roll Credits 

* John Shepherd
* Andrew Taylor
* Richard Buckland
* Ashesh Mahidadia
* Marc Chee
* Hayden Smith
* Robert Clifton-Everest
* Andrew Bennett
* Aarthi Natarajan

<table>
    <tr>
        <td><img src='imgs/chicken.png' width='300'></td>
        <td><img src='imgs/teamwork.png' width='300'></td>
    </tr>
    <tr>
        <td><img src='imgs/bstree.png' width='300'></td>
        <td><img src='imgs/coverimg.jpg' width='300'></td>
    </tr>
    <tr>
        <td><img src='imgs/beer.png' width='300'></td>
        <td><img src='imgs/textbook.png' width='300'></td>
    </tr>
</table>

</details>

Problems "Abstract File Editors", "A Visiting Calculator" and "Calculator Adapter" sourced from [School of Computer Science, University College Dublin](https://csserver.ucd.ie).