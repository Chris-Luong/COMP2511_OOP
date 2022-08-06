package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PenthouseRoom implements Room {

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
        // TODO Auto-generated method stub
        JSONArray bookings = new JSONArray();
        JSONObject roomBooking = new JSONObject();

        for (Booking oneBooking : this.bookings) {
            bookings.put(oneBooking.toJSON());
        }

        roomBooking.put("type", "penthouse");
        roomBooking.put("booking", bookings);

        return roomBooking;
    }

    @Override
    public void removeBooking(Booking booking) {
        // TODO Auto-generated method stub
        bookings.remove(booking);

    }

    @Override
    public Booking changeBooking(Booking booking, LocalDate arrival, LocalDate departure) {
        // TODO Auto-generated method stub
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
        System.out.println("Welcome to your penthouse apartment, complete with ensuite, lounge, kitchen and master bedroom.");
    }
    
}