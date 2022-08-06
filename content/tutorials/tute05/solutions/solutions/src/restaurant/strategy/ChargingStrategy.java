package restaurant.strategy;

import restaurant.Meal;

import java.util.List;

public interface ChargingStrategy {
    
    /**
     * The cost of a meal.
     */
    public double cost(List<Meal> order, boolean payeeIsMember);

    /**
     * Modifying factor of charges for standard customers.
     */
    public double standardChargeModifier();

}