# Design Smells
- Code is **tightly coupled** as a lot of the classes are linked and they all rely on Course to work.
A method in CourseOffering relies on Course which relies on Student which relies on Enrolement.
- This also means the code is **fragile** because in the scenario that Enrolment stops working, all
the other classes also stop working.
- There is a lot of **repetition** as methods defined in one class get redefined in another class
using the same code with an extra step (e.g. Course has a getCourseCode() method which returns
a variable courseCode and CourseOffering has the same method getCourseCode() which simply calls
the the method in Course by returning course.getCourseCode()).
- This also conflicts with the Law of Demeter as the code is not loosely coupled, with classes
calling methods from other classes within their own methods.
- CourseOffering also calls super() to create a course and then assigns this.course to the course
variable, which is **needless repetition**.
- Enrolment imports java.util.List but does not use it, which is **needless complexity**.
- **Opacity**: code has some confusing variable naming. None of the methods/classes
have documentation explaining their purpose and how to use them (e.g. Grade has a variable called
grade which is used in a method).

# Refactoring
- variables in some files like Grade were in a different order in the constructor so I changed this
to make it slightly less confusing.
- Removed the import java.util.list from Enrolment.
- String grade changed to String status in Grade because there is a Grade grade and a String grade
which is confusing at first.
- Removed extends Course in CourseOffering and removed the super() method.
- Long/heavily nested if and for statements like the ones in CourseOffering were replaced with
Java streams to reduce complexity.
- Added javadoc for methods that required explaining.
- Used extractiont to move code from CourseOffering to Course in the checkValidEnrolment method.