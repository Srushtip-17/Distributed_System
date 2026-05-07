// ============================================================
// FILE: sum_mpi.c
// PURPOSE: Calculate the SUM of an array using MPI
//          MPI = Message Passing Interface
//          Multiple processors work TOGETHER to find the sum
//          Each processor handles a PART of the array (parallel!)
//
// HOW IT WORKS:
//   Array [1,2,3,...,16] split across 4 processors:
//   Processor 0 → [1,2,3,4]   → partial sum = 10
//   Processor 1 → [5,6,7,8]   → partial sum = 26
//   Processor 2 → [9,10,11,12]→ partial sum = 42
//   Processor 3 → [13,14,15,16]→ partial sum = 58
//   Total = 10+26+42+58 = 136
// ============================================================

// MPI library — provides all MPI functions like MPI_Init, MPI_Scatter etc.
#include <mpi.h>

// Standard input/output — for printf
#include <stdio.h>

// Standard library — for malloc() and free()
#include <stdlib.h>

int main(int argc, char* argv[]) {

    // rank = ID of THIS processor (0, 1, 2, 3...)
    // size = TOTAL number of processors running
    // N    = total number of elements in the array
    int rank, size, N = 16;

    // array[]    = full array (only used by processor 0 / root)
    // local_sum  = sum calculated by THIS processor (its part only)
    // total_sum  = final sum of ALL parts (only stored at processor 0)
    int array[N], local_sum = 0, total_sum = 0;

    // -------------------------------------------------------
    // STEP 1: Initialize MPI
    // Must be called FIRST before any other MPI function
    // Sets up the communication environment for all processors
    // -------------------------------------------------------
    MPI_Init(&argc, &argv);

    // Get the RANK (ID) of this processor
    // rank = 0 means this is the ROOT (master) processor
    // rank = 1, 2, 3... are the worker processors
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    // Get the total number of processors (size)
    // MPI_COMM_WORLD = group of ALL processors in the program
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    // -------------------------------------------------------
    // STEP 2: Calculate how many elements each processor gets
    // If N=16 and size=4, each processor gets 16/4 = 4 elements
    // -------------------------------------------------------
    int elements_per_proc = N / size;

    // Allocate memory for this processor's chunk of the array
    // Each processor gets its own sub_array of size elements_per_proc
    int *sub_array = (int*)malloc(elements_per_proc * sizeof(int));

    // -------------------------------------------------------
    // STEP 3: ROOT processor (rank 0) initializes the full array
    // Only rank 0 creates the array — others skip this block
    // -------------------------------------------------------
    if (rank == 0) {
        // Fill array with values 1 to 16: [1, 2, 3, ..., 16]
        for (int i = 0; i < N; i++) {
            array[i] = i + 1;  // i+1 so we start from 1, not 0
        }
    }

    // -------------------------------------------------------
    // STEP 4: SCATTER — split and send the array to all processors
    // Root (rank 0) sends each processor its own chunk
    // MPI_Scatter(send_data, send_count, send_type,
    //             recv_data, recv_count, recv_type,
    //             root, communicator)
    //
    // Processor 0 gets array[0..3]  → sub_array = [1,2,3,4]
    // Processor 1 gets array[4..7]  → sub_array = [5,6,7,8]
    // Processor 2 gets array[8..11] → sub_array = [9,10,11,12]
    // Processor 3 gets array[12..15]→ sub_array = [13,14,15,16]
    // -------------------------------------------------------
    MPI_Scatter(
        array,            // Source: full array (only matters at root)
        elements_per_proc,// How many elements to send to each processor
        MPI_INT,          // Data type being sent
        sub_array,        // Destination: this processor's chunk
        elements_per_proc,// How many elements this processor receives
        MPI_INT,          // Data type being received
        0,                // Root processor (rank 0) is the sender
        MPI_COMM_WORLD    // All processors participate
    );

    // -------------------------------------------------------
    // STEP 5: Each processor computes its LOCAL (partial) sum
    // Every processor does this independently and in PARALLEL
    // -------------------------------------------------------
    for (int i = 0; i < elements_per_proc; i++) {
        local_sum += sub_array[i];  // Add each element to local sum
    }

    // Print each processor's partial result
    // All processors print their own rank and their partial sum
    printf("Processor %d - Partial Sum: %d\n", rank, local_sum);

    // -------------------------------------------------------
    // STEP 6: REDUCE — collect all partial sums into total sum
    // Each processor sends its local_sum to root (rank 0)
    // Root adds them all together using MPI_SUM operation
    //
    // MPI_Reduce(send_data, recv_data, count, datatype,
    //            operation, root, communicator)
    // -------------------------------------------------------
    MPI_Reduce(
        &local_sum,    // Each processor sends its local_sum
        &total_sum,    // Root stores the final total here
        1,             // Number of elements to reduce (just 1 sum)
        MPI_INT,       // Data type
        MPI_SUM,       // Operation: ADD all values together
        0,             // Root processor (rank 0) collects the result
        MPI_COMM_WORLD // All processors participate
    );

    // -------------------------------------------------------
    // STEP 7: Only ROOT (rank 0) prints the final total sum
    // Other processors have garbage in total_sum, so only rank 0 prints
    // -------------------------------------------------------
    if (rank == 0) {
        printf("Total Sum: %d\n", total_sum);
        // For N=16: sum = 1+2+...+16 = 136
    }

    // -------------------------------------------------------
    // STEP 8: Cleanup
    // -------------------------------------------------------

    // Free the dynamically allocated memory for sub_array
    free(sub_array);

    // Finalize MPI — must be called LAST before program exits
    // Cleans up all MPI resources and communication channels
    MPI_Finalize();

    return 0;
}

save by name -  sum_mpi.c
    
steps to run
run in linux 

sudo apt update
sudo apt install mpich

mpicc sum_mpi.c -o sum_mpi
mpirun -np 4 ./sum_mpi
