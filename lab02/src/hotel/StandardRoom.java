package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class StandardRoom implements Room {

    private List<Booking> bookings = new ArrayList<Booking>();

    @Override
    public Booking book(LocalDate arrival, LocalDate departure) {
        for (Booking booking : bookings) {
            if (booking.overlaps(arrival, departure)) {
                return null;
            }
        }

        Booking booking = new Booking(arrival, departure);
        bookings.add(booking);
        return booking;
    }

    @Override
    public JSONObject toJSON() {
        // TODO
        JSONArray bookings = new JSONArray();
        JSONObject roomBooking = new JSONObject();

        for (Booking oneBooking : this.bookings) {
            bookings.put(oneBooking.toJSON());
        }

        roomBooking.put("type", "standard");
        roomBooking.put("booking", bookings);
        
        return roomBooking;
    }

    @Override
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    @Override
    public Booking changeBooking(Booking booking, LocalDate arrival, LocalDate departure) {
        // TODO 
        for (Booking oneBooking : bookings) {
            if (oneBooking.overlaps(arrival, departure)) {
                return null;
            }
        }
        booking.setArrival(arrival);
        booking.setDeparture(departure);
        return booking;
    }

    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your standard room. Enjoy your stay :)");
    }
    
}