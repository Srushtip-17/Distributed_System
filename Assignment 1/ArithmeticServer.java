// ============================================================
// FILE: ArithmeticServer.java
// PURPOSE: This is the SERVER - it starts up, registers the
//          arithmetic service, and waits for client requests.
//          Run this FIRST before running the client.
// ============================================================

// "Naming" is used to register (bind) the service with a name
// so clients can find it by that name
import java.rmi.Naming;

// "LocateRegistry" is used to start the RMI Registry
// (Registry = like a phone book that maps service names to objects)
import java.rmi.registry.LocateRegistry;

public class ArithmeticServer {

    public static void main(String[] args) {

        try {
            // STEP 1: Start the RMI Registry on port 1099
            // Port 1099 is the DEFAULT port for Java RMI
            // Think of Registry as a "directory service" — clients look up services here
            LocateRegistry.createRegistry(1099);

            // STEP 2: Create an instance (object) of our arithmetic service
            // This object contains the actual add/subtract/multiply/divide logic
            ArithmeticServiceImpl arithmeticService = new ArithmeticServiceImpl();

            // STEP 3: Register (bind) the service with a name in the Registry
            // "rmi://localhost/ArithmeticService" is the address (URL format):
            //   - rmi://         → protocol (like http:// for websites)
            //   - localhost      → this same machine (server's IP)
            //   - /ArithmeticService → name clients will use to look it up
            // "rebind" replaces any existing binding with this name (safe re-registration)
            Naming.rebind("rmi://localhost/ArithmeticService", arithmeticService);

            // STEP 4: Print confirmation that server is up and waiting
            System.out.println("Arithmetic Server is ready...");

            // Server now just sits and waits for client calls automatically
            // (RMI handles threading in the background)

        } catch (Exception e) {
            // If anything goes wrong (port in use, registry error, etc.)
            // print the full error details
            e.printStackTrace();
        }
    }
}
