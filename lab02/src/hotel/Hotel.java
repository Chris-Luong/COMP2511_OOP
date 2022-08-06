package hotel;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class Hotel {

    private List<Room> rooms = new ArrayList<Room>();
    private String name;

    /**
     * Sets the hotel name
     * @param name
     */
    public Hotel(String name) {
        this.name = name;
    }

    /**
     * Adds room to rooms
     * @param room
     */
    public void AddRoom (Room room) {
        rooms.add(room);
    }

    /**
     * Makes a booking in any available room with the given preferences.
     * 
     * @param arrival
     * @param departure
     * @param standard - does the client want a standard room?
     * @param ensuite - does the client want an ensuite room?
     * @param penthouse - does the client want a penthouse room?
     * @return If there were no available rooms for the given preferences, returns false.
     * Returns true if the booking was successful
     */
    public boolean makeBooking(LocalDate arrival, LocalDate departure, boolean standard, boolean ensuite, boolean penthouse) {
        // Preferences given from lowest to highest
        if (standard) {
            for (Room room : rooms) {
                if (room instanceof StandardRoom) {
                    if (room.book(arrival, departure) != null) {
                        return true;
                    }
                }
            }
            return false;
        } else if (ensuite) {
            for (Room room : rooms) {
                if (room instanceof EnsuiteRoom) {
                    if (room.book(arrival, departure) != null) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (Room room : rooms) {
                if (room instanceof PenthouseRoom) {
                    if (room.book(arrival, departure) != null) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * @return A JSON Object of the form:
     * { "name": name, "rooms": [ each room as a JSON object, in order of creation ]}
     */
    public JSONObject toJSON() {
        JSONArray roomsArray = new JSONArray();
        JSONObject hotel = new JSONObject();

        for (Room room : rooms) {
            roomsArray.put(room.toJSON());
        }

        hotel.put("name", name);
        hotel.put("rooms", roomsArray);

        return hotel;
    }

    public static void main(String[] args) {
        Hotel hotel = new Hotel("Hotel1");

        assertEquals("Hotel1", hotel.name);

        LocalDate d1 = LocalDate.of(2021,9,25);
		LocalDate d2 = LocalDate.of(2021,9,28);

        Room room = new PenthouseRoom();
        hotel.AddRoom(room);

        LocalDate d3 = LocalDate.of(2021,9,27);
		LocalDate d4 = LocalDate.of(2021,9,30);

        // successive booking
        assertTrue(hotel.makeBooking(d1, d2, false, false, true));
        // failed booking
        assertFalse(hotel.makeBooking(d3, d4, false, false, true));

        LocalDate d5 = LocalDate.of(2021,10,27);
		LocalDate d6 = LocalDate.of(2021,10,30);
        
        // successful booking
        Booking booking = room.book(d5, d6);
        // failed booking
        assertNull(room.changeBooking(booking, d3, d4));
    }   
}