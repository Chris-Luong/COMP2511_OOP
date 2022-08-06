# Design Principles/Choices
- Principle of least knowledge: Satellite methods are able to use other methods within Satellite class.
- Cohesive: satellite, has 3 subclasses and super is abstract and has abstract methods.
- Coupling: Satellite only has 1 level of subclass so there is not too much dependency but they are all linked
to the main Satellite class so it is loosely coupled.
- ABSTRACTION: through encapsulation and use of methods like helper functions to simplify code.
- ENCAPSULATION: all variables are private so they can only be accessed through methods of the class.
- Parameter List: kept short with a maximum of 6-7 variables execept for Relay Satellite which has extra parameters for the region
it can travel in. While a lot of these parameters could have been removed by simply using numbers as the parameters for the
constructors of most classes (e.g. MAX_RANGE could just be written as 50000 for device in the constuctor), I chose to use static
final parameters as constants as it would be easier to interpret by other people reading my code and easier to edit if
these values change in the future, as opposed to just using numbers. This also aligns with the 'renaming variable' technique of refactoring.

# Design Smells
- Code was refactored a few times to reduce repetition, indicating the code can be changed easily without breaking
it (not rigid or fragile).
- This was achieved by making helper functions that could be used by different methods, 
and can also be used in the future/changed to match different requirements (code is not immoblie).
- The naming of functions was thoughtful in order to make it easier to understand (opacity), such as the
isInRange function which checks if an entity is in range of another one (using isVisible and getDistance), without
the user needing to know how it was implemented.

# UML
- https://stackoverflow.com/questions/47588511/uml-diagram-how-to-show-final?rq=1 was used as a model for static final
variables
- BlackoutController is linked to Satellite and Device by an aggregation relationship because Blackout 'has-a' list
of both these classes. The relationship is one-to-many because of this list.
- Similarly, both Device and Satellite are linked to File with a one-to-many aggregation relationship. I initially
thought they should be connected by a composition relationship because I assumed files were just plain-text files
which wouldn't be necessary for these entities to exist. However, by thinking of files as any form of text document
(e.g. Java/C files with methods for what to do), then these would be vital for the entities to work.
- The different types of satellites are subclasses of the main Satellite class and thus, are connected to Satellite
by a directed association link.
- The ShrinkinSatellite has a variable isQuantum which would check if a file has the keyword "quantum", however this
has not been implemented as I have not reached that stage. Implementing it would involve going through any file sent
to this satellite and making the appropriate changes if the keyword is found.

# BlackoutController
- For creating devices and satellites, I initially used if statements but they seemed very repetitive and clunky.
After doing some research, I implemented switch statements instead which makes the code cleaner and easier to read.
- When creating devices/satellites, I assumed that we would only be given valid types. However, if we are not always
given valid types, we can add one more case for the last type and have the default case to throw an exception.
- Added helper functions here for ease of accessbility and implementation, as some use both Satellite and Device
and I have not implemented a super class that contains both Satellite and Device. Possibly better to have a helper
class implemented/a super class for both entities or both these methods.
when refactoring code.
- Started adding more helper functions that could be used by multiple functions (using the DRY design principle)
such as retrieveSatellite.
- The function communicableEntitiesInRange(String id) was split to conform to (DRY and KISS principles). There is
very little code in this main function as it has been offloaded into another helper function that actually adds
the entitie within range. This helper function calls on another function to check whether an entity is within range
of another. Thus, repetition is avoided by using helper functions with small blocks of code, and things are simplified
by having easily understandable function names rather than large blocks of code in one main function. This is similar
to delegation but with methods instead of objects.
- Method overloading was used for these helper functions as they had slightly different implementations depending on
whether a device or satellite is given to the function.

# Satellite
- Both the Satellite and Device classes could be subclasses of an entity superclass in the future to reduce repetition
- Made Satellite an abstract class as there are many common features between different types of satellites so these
could be implemented in the main class. However, some methods are dependent on the type of satellite like getType()
so these would be implemented as abstract methods to be overridden by the subclasses.
- If a file is over 80 bytes for standard, reject straight away rather than sending until byte limit is reached
- for satellites that don't have a limit for files/bytes stored (i.e. they can't store files or they can store
as many as they want), the corresponding variable for the file/byte limit is set to -1 as a simple solution
- Relay: assume it can send files to any type of device. Since the clarification mentions it was late and will
not be tested, the implementation will be left like this.
- Relay: contains its own method for changing velocity as this method is unique to relay satellites. However,
if other entities end up having this property, the method can be placed in an interface
- Satellite has file limits and transfer rates as variables but they have not been used as I have not reached that
stage.

# Device
- Different categories had only minor differences so subclasses were not created for Device. Rather, the differences
were made clear in Blackout and used to create the different types of device.