package splitter;
import java.util.Scanner;  // Import the Scanner class

public class Splitter {

    public static void main(String[] args) {
        System.out.println("Enter a sentence specified by spaces only: ");
        // Add your code

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String str = myObj.nextLine();  // Read user input and store in string
        myObj.close(); // close scanner
        
        args = str.split("\\s+"); // split string
        
        // iterating over string and printing wiht new lines
        for (int i = 0; i < args.length; i++) {
            System.out.print(args[i] + "\n");
        }
    }
}
