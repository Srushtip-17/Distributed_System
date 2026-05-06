// ============================================================
// FILE: ArithmeticServiceImpl.java
// PURPOSE: This is the IMPLEMENTATION - the actual code that
//          does the math. It runs on the SERVER machine.
//          "Impl" = Implementation (a common Java naming convention)
// ============================================================

// Needed to throw network-related errors
import java.rmi.RemoteException;

// UnicastRemoteObject makes this object accessible over the network
// "Unicast" means one-to-one communication (one client talks to one server)
import java.rmi.server.UnicastRemoteObject;

// "extends UnicastRemoteObject" → enables this class to be called remotely
// "implements ArithmeticService"  → must provide all 4 methods from the interface
public class ArithmeticServiceImpl extends UnicastRemoteObject implements ArithmeticService {

    // CONSTRUCTOR: required because UnicastRemoteObject's constructor
    // throws RemoteException, so our constructor must also declare it
    public ArithmeticServiceImpl() throws RemoteException {
        // super() is called automatically — it registers this object
        // with the RMI runtime so it can receive remote calls
    }

    // METHOD: Add two numbers
    // var1 = first number, var3 = second number
    // (names like var1, var3 come from decompilation — normally they'd be a, b)
    public double add(double var1, double var3) throws RemoteException {
        return var1 + var3;  // Simple addition and return result
    }

    // METHOD: Subtract second number from first
    public double subtract(double var1, double var3) throws RemoteException {
        return var1 - var3;  // var1 minus var3
    }

    // METHOD: Multiply two numbers
    public double multiply(double var1, double var3) throws RemoteException {
        return var1 * var3;  // var1 times var3
    }

    // METHOD: Divide first number by second
    public double divide(double var1, double var3) throws RemoteException {

        // SAFETY CHECK: Division by zero is mathematically undefined
        // We throw a RemoteException (sends error back to the client)
        if (var3 == 0.0) {
            throw new RemoteException("Cannot divide by zero");
        } else {
            // If denominator is not zero, perform safe division
            return var1 / var3;
        }
    }
}
