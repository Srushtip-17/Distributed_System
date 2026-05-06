// ============================================================
// FILE: CalculatorServer.java
// PURPOSE: This is the SERVER — it starts up, creates the
//          Calculator object, and registers it so clients
//          can find and use it over the network.
//          Run this FIRST before running the client.
// ============================================================

// Import auto-generated classes from our IDL file (CalculatorApp folder)
// Contains: Calculator, CalculatorPOA, CalculatorHelper, DivisionByZero
import CalculatorApp.*;

// CORBA = Common Object Request Broker Architecture
// ORB (Object Request Broker) = the "middleman" that handles
// all network communication between client and server
import org.omg.CORBA.*;

// CosNaming = CORBA's Naming Service
// Like a phone book — server registers here, client looks up here
import org.omg.CosNaming.*;

// PortableServer = allows creating server-side objects (servants)
// POA = Portable Object Adapter — manages server objects
import org.omg.PortableServer.*;

// ============================================================
// CLASS: CalculatorImpl
// PURPOSE: The ACTUAL implementation of the calculator.
//          Contains the real math logic (add, subtract, etc.)
//          "extends CalculatorPOA" = generated from IDL, makes
//          this class available as a CORBA server object
// ============================================================
class CalculatorImpl extends CalculatorPOA {

    // Store a reference to the ORB (needed for shutdown etc.)
    private ORB orb;

    // METHOD: Called to give this object access to the ORB
    public void setORB(ORB orb_val) {
        orb = orb_val;  // Save the ORB reference for later use
    }

    // METHOD: Add two numbers — simple math, return result
    public float add(float a, float b) {
        return a + b;
    }

    // METHOD: Subtract b from a — return result
    public float subtract(float a, float b) {
        return a - b;
    }

    // METHOD: Multiply two numbers — return result
    public float multiply(float a, float b) {
        return a * b;
    }

    // METHOD: Divide a by b
    // "throws DivisionByZero" — this exception is defined in our IDL
    // and auto-generated into Java. It gets sent back to the client.
    public float divide(float a, float b) throws DivisionByZero {
        // SAFETY CHECK: Cannot divide by zero
        if (b == 0) throw new DivisionByZero();  // Send error to client
        return a / b;  // Safe to divide
    }
}

// ============================================================
// CLASS: CalculatorServer
// PURPOSE: Main class that starts the server, sets up CORBA,
//          and registers the Calculator with the Naming Service
// ============================================================
public class CalculatorServer {

    public static void main(String args[]) {
        try {
            // STEP 1: Initialize the ORB (Object Request Broker)
            // ORB is the core of CORBA — handles all network communication
            // "args" passes command-line arguments (like -ORBInitialPort 1050)
            ORB orb = ORB.init(args, null);

            // STEP 2: Get the RootPOA from the ORB
            // POA (Portable Object Adapter) manages server-side objects
            // "resolve_initial_references" finds built-in CORBA services
            // "RootPOA" is the main POA that manages all our server objects
            POA rootpoa = POAHelper.narrow(
                orb.resolve_initial_references("RootPOA")
            );

            // Activate the POA Manager — starts accepting client requests
            // Without this, the server won't process any incoming calls
            rootpoa.the_POAManager().activate();

            // STEP 3: Create an instance of our Calculator implementation
            CalculatorImpl calculator = new CalculatorImpl();

            // Give the calculator object access to the ORB
            calculator.setORB(orb);

            // STEP 4: Convert our Java object into a CORBA object reference
            // "servant_to_reference" registers the object with the POA
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calculator);

            // Narrow (cast) the generic CORBA reference to a Calculator type
            Calculator href = CalculatorHelper.narrow(ref);

            // STEP 5: Find the Naming Service (like a phone book)
            // Clients will look up "Calculator" here to find our server object
            org.omg.CORBA.Object objRef =
                orb.resolve_initial_references("NameService");

            // Narrow to NamingContextExt (extended naming context with more features)
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // STEP 6: Create a name path "Calculator" and bind our object to it
            // "to_name" converts string "Calculator" into CORBA NameComponent array
            NameComponent path[] = ncRef.to_name("Calculator");

            // "rebind" registers our Calculator under the name "Calculator"
            // rebind = replace if already exists (safe re-registration)
            ncRef.rebind(path, href);

            // STEP 7: Print confirmation and start listening for client calls
            System.out.println("Calculator Server is running...");

            // orb.run() = blocks here and keeps server alive
            // Server now waits forever for client requests
            orb.run();

        } catch (Exception e) {
            // Print any errors that occur during startup
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
