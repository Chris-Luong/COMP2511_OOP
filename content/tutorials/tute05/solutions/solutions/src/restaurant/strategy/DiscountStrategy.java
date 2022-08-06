package restaurant.strategy;

import java.util.List;

import restaurant.Meal;

public class DiscountStrategy implements ChargingStrategy {

    private final double STANDARD_CHARGE = 1;

    @Override
    public double cost(List<Meal> order, boolean payeeIsMember) {
        double modifier = payeeIsMember ? 0.85 : 1;
        return order.stream().mapToDouble(m -> m.getCost() * modifier).sum();
    }

    @Override
    public double standardChargeModifier() {
        return STANDARD_CHARGE;
    }
    
}