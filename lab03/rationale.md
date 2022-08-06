# Rationale - Lab 03 Cars - Christopher Luong

Make sure you articulate each of your decisions in terms of 
* What you chose to do
* Why you chose to do it - here it is good to draw on course concepts

* I have done XXX because XXX

e.g. I have used an interface over an abstract class because ...,
there is an aggregation between X and Y because ...

- The Car is connected to the Owner class by a 1-1 aggregation, based on the
assumption that a car only has one owner and thus, a Car contains one Owner.

- Similarly, the Car is also connected to the Manufacturer class by a 1-1
aggregation because of the same reasoning as the Car to Owner relationship.

- There is a composition between Car and Engine because a car cannot function
without an engine. There is also a 1-to-many relationship between these 2
classes because a car can have various engines within it.

- I have used an abstract class for Engine because different types of engines
have different speeds which need to be set, so they all have a common function
that would be implemented differently.

- There is a directed association between the types of engine classes and the
main Engine class because they are all subclasses which use the variables and
method defined in Engine.

- Both TimeTravelling and Flying are interfaces because there is a car that
can time-travel and fly so it would need to implement both of these. Another
reason is because they are also behaviours that can apply to any vehicle
according to the spec.

- The different types of cars have directed association with the main class
Car, similar to how differnt types of engines are associated with the main
class Engine.

- The Plane is a separate class because there is no mention of a relationship
with the Car and it also would not share all the properties of a car (e.g.
different manufacturer).