#!/bin/bash

# Define paths
OUTPUT_FILE="/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumbers.csv"
MAX_VALUE=100000
NUM_PER_ROW=1000

# Print debug information
echo "Arguments received: $*"
echo "Generating $NUM_COUNT random numbers..."

# Validate the input
NUM_COUNT=$1
if ! [[ "$NUM_COUNT" =~ ^[0-9]+$ ]] || [ "$NUM_COUNT" -le 0 ]; then
    echo "Invalid input. Please enter a positive integer."
    exit 1
fi

# Remove any existing output file
rm -f $OUTPUT_FILE

# Generate random numbers using a loop and $RANDOM
{
    for ((i = 0; i < NUM_COUNT; i++)); do
        RANDOM_NUMBER=$((RANDOM % (MAX_VALUE + 1)))
        echo -n "$RANDOM_NUMBER"
        if (( (i + 1) % NUM_PER_ROW == 0 )); then
            echo
        else
            echo -n ", "
        fi
    done
} > $OUTPUT_FILE

echo "Random numbers generated and stored in $OUTPUT_FILE"