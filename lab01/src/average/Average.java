package average;

public class Average {
    /**
     * Returns the average of an array of numbers
     * 
     * @param the array of integer numbers
     * @return the average of the numbers
     */
    public float computeAverage(int[] nums) {
        float result = 0;

        // Add your code
        for (int i = 0; i < nums.length; i++) {
            result += nums[i];
        }
        
        return result / nums.length;
    }

    public static void main(String[] args) {
        // Add your code
        //int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] array = {1, 2, 5, 5, 5, 2, 10, 5, 3};

        Average obj = new Average();

        float average = obj.computeAverage(array);

        System.out.println(average);
    }
}
