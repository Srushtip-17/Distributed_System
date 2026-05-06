// ============================================================
// FILE: ArithmeticService.java
// PURPOSE: This is the INTERFACE - like a menu/contract that
//          defines WHAT operations are available remotely.
//          Both the server and client use this file.
// ============================================================

// "Remote" means this interface can be called over a network
import java.rmi.Remote;

// "RemoteException" is thrown when something goes wrong over the network
import java.rmi.RemoteException;

// "extends Remote" tells Java: "This interface can be used over a network (RMI)"
// RMI = Remote Method Invocation (calling methods on another computer)
public interface ArithmeticService extends Remote {

    // METHOD: add two numbers and return the result
    // "throws RemoteException" is MANDATORY for all RMI methods
    // because network calls can fail at any time
    double add(double a, double b) throws RemoteException;

    // METHOD: subtract second number from first
    double subtract(double a, double b) throws RemoteException;

    // METHOD: multiply two numbers
    double multiply(double a, double b) throws RemoteException;

    // METHOD: divide first number by second
    // (division by zero is handled on the server side)
    double divide(double a, double b) throws RemoteException;
}
