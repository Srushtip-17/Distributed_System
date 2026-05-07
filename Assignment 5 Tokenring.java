import java.util.Scanner;  // Import Scanner class to take input from user

// ─────────────────────────────────────────────────────────
//  WHAT IS THIS PROGRAM?
//  This is the TOKEN RING algorithm in Distributed Systems.
//
//  CONCEPT:
//  - All computers (nodes) are connected in a RING shape: 0→1→2→3→0
//  - A special signal called a TOKEN moves around the ring
//  - Only the node that HAS the token can SEND data
//  - If sender and receiver are not neighbors, data is FORWARDED
//    node by node around the ring until it reaches the receiver
// ─────────────────────────────────────────────────────────


public class Tokenring {

    public static void main(String args[]) {

        Scanner scanner = new Scanner(System.in);  // Create Scanner object to read keyboard input

        // ── STEP 1: Build the Ring ────────────────────────
        System.out.print("Enter Number Of Node: ");
        int numberOfNode = scanner.nextInt();  // Read how many nodes (computers) are in the ring

        // Print the ring structure: 0-1-2-3-...-0
        System.out.print("Ring Formed: ");
        for (int i = 0; i < numberOfNode; i++) {
            System.out.print(i + "-");  // Print each node number followed by "-"
        }
        System.out.println("0");  // Ring closes back to node 0 (circular)

        int token = 0;  // Token starts at node 0 (node 0 holds the token initially)

        // ── STEP 2: Keep Sending Data Forever (infinite loop) ──
        while (true) {

            // Take input: who is sending, who is receiving, what data
            System.out.print("Enter Sender: ");
            int sender = scanner.nextInt();    // Node that wants to send data

            System.out.print("Enter Receiver: ");
            int receiver = scanner.nextInt();  // Node that should receive the data

            System.out.print("Enter Date To Send: ");
            int data = scanner.nextInt();      // The actual data/message to send

            // ── STEP 3: Pass Token to the Sender ─────────
            // Token must travel from its current position to the sender node
            System.out.print("Token Passing: ");
            for (int i = token; i < sender; i++) {
                System.out.print(i + "->");  // Show token moving step by step to sender
            }

            // ── STEP 4: Send the Data ─────────────────────
            // CASE 1: Receiver is the NEXT node right after sender (direct neighbors)
            if (receiver == sender + 1) {
                // No forwarding needed — direct delivery!
                System.out.println("Sender: " + sender + " Sending The Data: " + data);
                System.out.println("Receiver: " + receiver + " Received The Data: " + data);

            } else {
                // CASE 2: Receiver is NOT the next node — data must be FORWARDED
                System.out.println(sender);  // Print sender node (end of token passing line)

                System.out.println("Sender: " + sender + " Sending The Data: " + data);

                // Forward data ring by ring until we reach the receiver
                // i = (i+1) % numberOfNode  →  wraps around (e.g., after node 3 comes node 0)
                for (int i = sender; i != receiver; i = (i + 1) % numberOfNode) {
                    System.out.println("Data: " + data + " Forwared By: " + i);
                    // Each intermediate node forwards the data to the next
                }

                System.out.println("Receiver: " + receiver + " Received The Data: " + data);
            }

            // ── STEP 5: Update Token Position ─────────────
            // After sending, token stays at the sender node
            token = sender;

            // Loop repeats → ask for next sender/receiver/data
        }
    }
}


steps to run
javac Tokenring.java
java Tokenring
