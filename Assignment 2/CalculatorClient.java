// ============================================================
// FILE: CalculatorClient.java
// PURPOSE: This is the CLIENT — it connects to the server
//          through CORBA's Naming Service, finds the Calculator
//          object, and calls its methods over the network.
//          Run this AFTER the server is already running.
// ============================================================

// Import auto-generated classes from our IDL file
// Contains: Calculator, CalculatorHelper, DivisionByZero
import CalculatorApp.*;

// ORB = Object Request Broker — handles all CORBA network communication
import org.omg.CORBA.*;

// CosNaming = Naming Service — used to LOOK UP the server object by name
import org.omg.CosNaming.*;

public class CalculatorClient {

    public static void main(String args[]) {
        try {
            // STEP 1: Initialize the ORB (same as server side)
            // ORB is the core engine of CORBA communication
            // "args" should include: -ORBInitialHost localhost -ORBInitialPort 1050
            ORB orb = ORB.init(args, null);

            // STEP 2: Connect to the Naming Service
            // "NameService" is a built-in CORBA service — like a phone book
            // The server registered "Calculator" here, we look it up here
            org.omg.CORBA.Object objRef =
                orb.resolve_initial_references("NameService");

            // "narrow" = cast the generic CORBA object to NamingContextExt
            // NamingContextExt gives us extra methods like resolve_str()
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // STEP 3: Look up "Calculator" in the Naming Service
            // "resolve_str" finds the object registered under name "Calculator"
            // This returns the server's Calculator object — but over the NETWORK!
            // "narrow" casts it to Calculator type so we can call add/subtract/etc.
            Calculator calculator = CalculatorHelper.narrow(
                ncRef.resolve_str("Calculator")
            );
            // Now "calculator" looks like a local object but every method call
            // actually travels OVER THE NETWORK to the server!

            // STEP 4: Call remote methods — results come back from server
            // Each of these calls goes to the SERVER machine and returns the result

            // Call add() on the server — server computes 10+5=15, sends back 15.0
            System.out.println("Addition (10 + 5): " + calculator.add(10, 5));

            // Call subtract() on the server — server computes 10-5=5, sends back 5.0
            System.out.println("Subtraction (10 - 5): " + calculator.subtract(10, 5));

            // Call multiply() on the server — server computes 10*5=50, sends back 50.0
            System.out.println("Multiplication (10 * 5): " + calculator.multiply(10, 5));

            try {
                // Call divide() — server computes 10/2=5.0, sends back 5.0 (normal)
                System.out.println("Division (10 / 2): " + calculator.divide(10, 2));

                // Call divide() with 0 — server throws DivisionByZero exception
                // This exception travels OVER THE NETWORK back to the client!
                System.out.println("Division (10 / 0): " + calculator.divide(10, 0));

            } catch (DivisionByZero e) {
                // Catch the custom exception that came from the SERVER
                // DivisionByZero is defined in our IDL and auto-generated
                System.out.println("Error: Division by zero is not allowed!");
            }

        } catch (Exception e) {
            // Handle any connection or CORBA errors
            // Common: "Cannot find NameService" = server not running
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
