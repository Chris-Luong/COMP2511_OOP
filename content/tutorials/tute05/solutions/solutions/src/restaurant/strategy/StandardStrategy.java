package restaurant.strategy;

import java.util.List;

import restaurant.Meal;

public class StandardStrategy implements ChargingStrategy {

    private final double STANDARD_RATE = 1;

    @Override
    public double cost(List<Meal> order, boolean payeeIsMember) {
        return order.stream().mapToDouble(m -> m.getCost()).sum();
    }

    @Override
    public double standardChargeModifier() {
        return STANDARD_RATE;
    }
    
}