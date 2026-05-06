import random

# Simulating network nodes with their clock times (in seconds)
class Node:
    def __init__(self, node_id, clock_time):
        self.node_id = node_id
        self.clock_time = clock_time

    def get_time(self):
        return self.clock_time

    def adjust_time(self, offset):
        self.clock_time += offset

    def __repr__(self):
        return f"Node {self.node_id}: {self.clock_time:.2f} sec"


class Coordinator(Node):
    def synchronize_clocks(self, nodes):
        print("Polling times from all nodes...")
        times = {}

        # Step 1: Collect time differences
        for node in nodes:
            if node.node_id == self.node_id:
                continue
            # Simulate network delay
            delay = random.uniform(0.1, 0.5)
            node_time = node.get_time() + delay / 2  # assume symmetric delay
            times[node.node_id] = node_time - self.clock_time
            print(f"Node {node.node_id} time: {node.get_time():.2f}, adjusted for delay: {node_time:.2f}")

        # Add coordinator itself
        times[self.node_id] = 0.0

        # Step 2: Compute average offset (excluding outliers if needed)
        average_offset = sum(times.values()) / len(times)
        print(f"\nCalculated average offset: {average_offset:.2f} seconds")

        # Step 3: Send adjustment to each node
        for node in nodes:
            offset = average_offset - times[node.node_id]
            node.adjust_time(offset)
            print(f"Adjusting Node {node.node_id} by {offset:+.2f} seconds")

        print("\nSynchronization complete.\n")


# --- Example usage ---
if __name__ == "__main__":
    # Create nodes with different clock times
    nodes = [
        Coordinator(0, 100.0),   # Coordinator
        Node(1, 102.0),
        Node(2, 98.5),
        Node(3, 101.2),
        Node(4, 99.8),
    ]

    print("Initial times:")
    for node in nodes:
        print(node)

    coordinator = nodes[0]
    coordinator.synchronize_clocks(nodes)

    print("Final times after synchronization:")
    for node in nodes:
        print(node)

Python code.py
