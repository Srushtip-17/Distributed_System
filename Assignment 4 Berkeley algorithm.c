import random  # Import random module to simulate network delays (random numbers)

# ─────────────────────────────────────────────────────────
#  WHAT IS THIS PROGRAM?
#  This is the BERKELEY CLOCK SYNCHRONIZATION ALGORITHM.
#  In distributed systems, different computers (nodes) have
#  slightly different clock times. This algorithm fixes that
#  by making all clocks agree on a common average time.
#
#  HOW IT WORKS (3 simple steps):
#  1. Coordinator asks every node: "What time is it?"
#  2. Coordinator calculates the AVERAGE time of all nodes
#  3. Coordinator tells each node: "Adjust your clock by X"
# ─────────────────────────────────────────────────────────


# ── CLASS: Node ──────────────────────────────────────────
# A Node = one computer in the distributed system
# Each computer has its own clock (which may be slightly off)
class Node:

    def __init__(self, node_id, clock_time):
        # __init__ is the constructor — runs when we create a Node object
        self.node_id = node_id        # Unique ID number for this computer (e.g. 1, 2, 3)
        self.clock_time = clock_time  # Current clock time of this computer (in seconds)

    def get_time(self):
        # Returns this node's current clock time
        return self.clock_time

    def adjust_time(self, offset):
        # Adjusts (corrects) the clock by adding the offset value
        # offset can be positive (clock was slow) or negative (clock was fast)
        self.clock_time += offset

    def __repr__(self):
        # This controls how the Node is printed (e.g. "Node 1: 100.50 sec")
        # :.2f means show 2 decimal places
        return f"Node {self.node_id}: {self.clock_time:.2f} sec"


# ── CLASS: Coordinator ───────────────────────────────────
# Coordinator is a SPECIAL Node — it is the "master" computer
# It inherits everything from Node (same clock, same ID)
# BUT it has extra power: it can synchronize all other nodes
class Coordinator(Node):

    def synchronize_clocks(self, nodes):
        # This is the main method that runs the Berkeley Algorithm
        # 'nodes' = list of ALL computers (including the coordinator itself)

        print("Polling times from all nodes...")  # Tell user we are starting

        times = {}  # Empty dictionary to store time differences of each node
                    # Format: { node_id : time_difference_from_coordinator }

        # ── STEP 1: Collect time from each node ──────────
        for node in nodes:  # Loop through every computer in the network

            if node.node_id == self.node_id:
                continue  # Skip coordinator itself (we handle it separately below)

            # Simulate a random network delay between 0.1 and 0.5 seconds
            # (In real life, messages take time to travel over the network)
            delay = random.uniform(0.1, 0.5)

            # We assume delay is SYMMETRIC (same going and coming back)
            # So we add only HALF the delay to estimate the node's actual time
            node_time = node.get_time() + delay / 2

            # Calculate how far this node's clock is from the coordinator's clock
            # Positive value = node is AHEAD of coordinator
            # Negative value = node is BEHIND coordinator
            times[node.node_id] = node_time - self.clock_time

            # Print the node's raw time and its delay-adjusted time
            print(f"Node {node.node_id} time: {node.get_time():.2f}, adjusted for delay: {node_time:.2f}")

        # Add the coordinator's own offset as 0 (it is our reference point)
        times[self.node_id] = 0.0

        # ── STEP 2: Calculate the Average Offset ─────────
        # Add up all the differences and divide by number of nodes
        # This gives us the "average correction needed"
        average_offset = sum(times.values()) / len(times)

        print(f"\nCalculated average offset: {average_offset:.2f} seconds")  # Show the average

        # ── STEP 3: Send Adjustment to Each Node ─────────
        for node in nodes:  # Loop through all nodes again

            # Each node needs a DIFFERENT adjustment:
            # = (average offset) - (this node's current offset)
            # This moves every node towards the common average time
            offset = average_offset - times[node.node_id]

            node.adjust_time(offset)  # Apply the correction to the node's clock

            # Print how much each node was adjusted
            # :+.2f means always show + or - sign with 2 decimal places
            print(f"Adjusting Node {node.node_id} by {offset:+.2f} seconds")

        print("\nSynchronization complete.\n")  # Done!


# ── MAIN PROGRAM ─────────────────────────────────────────
# This block runs only when you directly execute this file
# (not when it's imported by another file)
if __name__ == "__main__":

    # Create a list of 5 nodes with different (unsynchronized) clock times
    # Node 0 is the Coordinator (master), Nodes 1–4 are regular workers
    nodes = [
        Coordinator(0, 100.0),   # Coordinator — clock shows 100.0 sec
        Node(1, 102.0),          # Node 1 — clock is 2 seconds AHEAD
        Node(2, 98.5),           # Node 2 — clock is 1.5 seconds BEHIND
        Node(3, 101.2),          # Node 3 — clock is 1.2 seconds AHEAD
        Node(4, 99.8),           # Node 4 — clock is 0.2 seconds BEHIND
    ]

    # Print all clock times BEFORE synchronization
    print("Initial times:")
    for node in nodes:
        print(node)   # Calls __repr__ which prints "Node X: Y.YY sec"

    # Get the coordinator (first item in list) and run synchronization
    coordinator = nodes[0]
    coordinator.synchronize_clocks(nodes)  # Run the Berkeley Algorithm

    # Print all clock times AFTER synchronization (should be close to each other now)
    print("Final times after synchronization:")
    for node in nodes:
        print(node)



steps to run
Step 1: Open terminal/command prompt
Step 2: Navigate to where your file is saved
cd Downloads
(or wherever you saved the file)
Step 3: Run the file
python Assignment1_Berkeley_Clock_Sync.py
or if that doesn't work, try:
python3 Assignment1_Berkeley_Clock_Sync.py
