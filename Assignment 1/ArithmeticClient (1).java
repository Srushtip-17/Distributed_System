// ============================================================
// FILE: ArithmeticClient.java
// PURPOSE: This is the CLIENT - it connects to the server,
//          takes input from the user, sends the numbers to the
//          server, and displays the result.
//          Run this AFTER the server is already running.
// ============================================================

// "Naming" is used to FIND (lookup) the service by its name in the registry
import java.rmi.Naming;

// Scanner is used to read user input from the keyboard
import java.util.Scanner;

public class ArithmeticClient {

    public static void main(String[] args) {

        try {
            // STEP 1: Connect to the server and get the remote service object
            // Naming.lookup() searches the RMI Registry for a service at this address:
            //   - rmi://localhost → the server machine (same machine here)
            //   - /ArithmeticService → the name the server registered with
            // Cast to ArithmeticService so we can call add/subtract/etc.
            ArithmeticService arithmeticService =
                (ArithmeticService) Naming.lookup(
                    "rmi://localhost/ArithmeticService"
                );
            // Now "arithmeticService" looks like a local object but
            // every method call actually goes over the NETWORK to the server!

            // STEP 2: Create a Scanner to read keyboard input from the user
            Scanner scanner = new Scanner(System.in);

            // STEP 3: Ask user to enter the first number
            System.out.print("Enter the first number: ");
            double num1 = scanner.nextDouble();  // Read and store first number

            // STEP 4: Ask user to enter the second number
            System.out.print("Enter the second number: ");
            double num2 = scanner.nextDouble();  // Read and store second number

            // STEP 5: Display operation menu to the user
            System.out.println("\nChoose an operation:");
            System.out.println("1. Add");
            System.out.println("2. Subtract");
            System.out.println("3. Multiply");
            System.out.println("4. Divide");
            System.out.print("Enter your choice (1-4): ");
            int choice = scanner.nextInt();  // Read menu choice

            // STEP 6: Call the appropriate REMOTE method based on user's choice
            // These calls go OVER THE NETWORK to the server!
            double result = 0;  // Will hold the answer returned from server
            switch (choice) {
                case 1:
                    result = arithmeticService.add(num1, num2);       // Remote call: addition
                    break;
                case 2:
                    result = arithmeticService.subtract(num1, num2);  // Remote call: subtraction
                    break;
                case 3:
                    result = arithmeticService.multiply(num1, num2);  // Remote call: multiplication
                    break;
                case 4:
                    result = arithmeticService.divide(num1, num2);    // Remote call: division
                    break;
                default:
                    // If user enters anything other than 1-4
                    System.out.println("Invalid choice");
                    return;  // Exit the program
            }

            // STEP 7: Display the result received FROM the server
            System.out.println("Result: " + result);

            // STEP 8: Close the scanner to free up resources
            scanner.close();

        } catch (Exception e) {
            // Handles errors like:
            // - Server is not running (connection refused)
            // - Division by zero (RemoteException from server)
            // - Wrong input type from user (InputMismatchException)
            e.printStackTrace();
        }
    }
}
