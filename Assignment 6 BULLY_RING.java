import java.util.Scanner;  // Import Scanner to take input from keyboard

// ─────────────────────────────────────────────────────────
//  WHAT IS THIS PROGRAM?
//  This is the BULLY ELECTION ALGORITHM in Distributed Systems.
//
//  CONCEPT:
//  - When a coordinator (leader) fails, a new one must be elected
//  - Any active process can START the election
//  - The process with the HIGHEST ID that is ACTIVE wins
//  - Higher ID "bullies" (beats) lower ID processes
// ─────────────────────────────────────────────────────────

public class Bully {

    static int n;          // Total number of processes in the system
    static int[] process;  // Array to store status of each process (1=Active, 0=Dead)

    // ── ELECTION METHOD ──────────────────────────────────
    // This method runs the election starting from 'initiator' process
    static void election(int initiator) {

        int coordinator = initiator;  // Assume initiator is coordinator at first

        System.out.println("\nElection Process:");  // Print heading

        // Loop from initiator's index to the last process
        // Check all processes with HIGHER ID than initiator
        for (int i = initiator; i < n; i++) {

            // Initiator sends election message to every higher process
            System.out.println("Process P" + initiator +
                    " sends message to P" + (i + 1));

            // If this higher process is ALIVE (status = 1), it can be coordinator
            if (process[i] == 1) {
                coordinator = i + 1;  // Update coordinator to this higher active process
                                      // i+1 because array index starts at 0 but process ID starts at 1
            }
        }

        // After checking all — the highest active process becomes coordinator
        System.out.println("\nProcess P" + coordinator +
                " becomes Coordinator");
    }

    // ── MAIN METHOD ──────────────────────────────────────
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);  // Create Scanner object for keyboard input

        System.out.print("Enter number of processes: ");
        n = sc.nextInt();  // Read total number of processes

        process = new int[n];  // Create array of size n to store each process status

        System.out.println("Enter process status (1=Active, 0=Dead):");

        // Loop to read status of each process one by one
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + ": ");  // Print process name (P1, P2, ...)
            process[i] = sc.nextInt();                // Read 1 (active) or 0 (dead)
        }

        System.out.print("Enter process initiating election: ");
        int initiator = sc.nextInt();  // Read which process is starting the election

        // Check if the initiator process is alive or dead
        if (process[initiator - 1] == 0) {
            // initiator-1 because array index starts at 0 (P1 is at index 0)
            System.out.println("Initiator process is dead");  // Can't start election if dead
        } else {
            election(initiator);  // Initiator is alive → start the election
        }

        sc.close();  // Close scanner to free resources
    }
}


Steps to run
javac Bully.java
java Bully
