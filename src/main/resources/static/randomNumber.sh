#!/bin/bash

# Define paths
OUTPUT_FILE="/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumbers.csv"
NUM_COUNT=100000
MAX_VALUE=100000
NUM_PER_ROW=1000

echo "Generating random numbers..."

# Remove any existing output file
rm -f $OUTPUT_FILE

# Generate random numbers using awk
awk -v num_count="$NUM_COUNT" -v max_value="$MAX_VALUE" -v num_per_row="$NUM_PER_ROW" '
BEGIN {
    srand();  # Seed the random number generator
    for (i = 0; i < num_count; i++) {
        printf "%d", int(rand() * (max_value + 1));
        if ((i + 1) % num_per_row == 0) {
            printf "\n";  # Add a newline after every num_per_row numbers
        } else {
            printf ", ";
        }
    }
}
' > $OUTPUT_FILE

echo "Random numbers generated and stored in $OUTPUT_FILE"