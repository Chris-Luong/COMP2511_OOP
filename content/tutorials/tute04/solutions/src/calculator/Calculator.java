package calculator;

public class Calculator {
    
    /**
     * @preconditions a, b != null
     * @postconditions a + b
     */
    public static Double add(Double a, Double b) {
        return a + b;
    }

    /**
     * @preconditions a, b != null
     * @postconditions a - b
     */
    public static Double subtract(Double a, Double b) {
        return a - b;
    }

    /**
     * @preconditions a, b != null
     * @postconditions a * b
     */
    public static Double multiply(Double a, Double b) {
        return a * b;
    }

    /**
     * @preconditions a, b != null, b != 0
     * @postconditions a / b
     */
    public static Double divide(Double a, Double b) {
        return a / b;
    }

    /**
     * @preconditions angle != null
     * @postconditions sin(angle)
     */
    public static Double sin(Double angle) {
        return Math.sin(angle);
    }

    /**
     * @preconditions angle != null
     * @postconditions cos(angle)
     */
    public static Double cos(Double angle) {
        return Math.cos(angle);
    }

    /**
     * @preconditions angle != null, angle != Math.PI / 2
     * @postconditions tan(angle)
     */
    public static Double tan(Double angle) {
        return Math.tan(angle);
    }

}