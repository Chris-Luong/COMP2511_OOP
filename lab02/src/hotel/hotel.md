Hotel.java Code Smells:

- Not all imports not used
- No constructors
- No setters and getters
- No private definitions

1) Added setter and getter for arrival and departure
2) Completed implementation of changeBooking
3) Completed implementation of JSONObject to JSON
4) Completed implementation of overlaps method f booking
5) Completed Hotel.java implementation


// Assumptions
- overlapping does not take into account the end time e.g. if
someone finishes on the Jan 3rd there can be a booking that starts on 3rd;
but they cannot making a booking on the 2nd if there is a booking from the
1st to 3rd.
- If someone chooses all 3 preferences for the room type, they will get the
lowest preference first (standard, then ensuite, then penthouse).