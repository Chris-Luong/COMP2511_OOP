package hotel;

import java.time.LocalDate;

import org.json.JSONObject;

public class Booking {
    
    LocalDate arrival;
    LocalDate departure;

    public Booking(LocalDate arrival, LocalDate departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    /**
     * @return a JSONObject of the form {"arrival": arrival, "departure": departure}
     */
    public JSONObject toJSON() {
        JSONObject booking = new JSONObject();
        booking.put("arrival", arrival);
        booking.put("departure", departure);

        return booking;
    }

    /**
     * Checks if there is an overlap in booking times
     * @param start
     * @param end
     * @return
     */
    public boolean overlaps(LocalDate start, LocalDate end) {
        return start.isBefore(arrival) && end.isBefore(departure) ||
                start.isAfter(arrival) && end.isAfter(departure) ||
                (start.isAfter(arrival) && start.isBefore(departure) 
                && end.isAfter(arrival) && end.isBefore(departure));
    }

    /**
     * Get time of arrival
     * @return
     */
    public LocalDate getArrival() {
        return arrival;
    }

    /**
     * Setter for time of arrival
     * @param arrival
     */
    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    /**
     * Get time of departure
     * @return
     */
    public LocalDate getDeparture() {
        return departure;
    }

    /**
     * Setter for time of departure
     * @param departure
     */
    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

}